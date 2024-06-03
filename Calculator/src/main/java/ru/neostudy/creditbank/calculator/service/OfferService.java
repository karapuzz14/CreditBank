package ru.neostudy.creditbank.calculator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.neostudy.creditbank.calculator.dto.LoanOfferDto;
import ru.neostudy.creditbank.calculator.dto.LoanStatementRequestDto;
import ru.neostudy.creditbank.calculator.exception.LaterBirthdateException;

/**
 * Сервис для расчёта 4-х возможных условий кредита, ежемесячных аннуитетных платежей, полной
 * стоимости кредита.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OfferService {

  @Value("${rate}")
  private BigDecimal rate;

  /**
   * Проверяет совершеннолетие пользователя.
   *
   * @param date Дата рождения пользователя
   * @throws LaterBirthdateException Ошибка - пользователь несовершеннолетний.
   */
  public void isDateLate(LocalDate date) throws LaterBirthdateException {
    LocalDate checkpoint = LocalDate.now().minusYears(18);
    if (date.isAfter(checkpoint)) {
      throw new LaterBirthdateException("Некорректно указана дата рождения: '" + date
          + "'. Пользователь несовершеннолетний.",
          new Date());
    }
  }

  /**
   * Рассчитывает список из 4-х возможных предложений по кредиту в зависимости от наличия страховки
   * по кредиту и статуса зарплатного клиента.
   *
   * @param request Запрос на кредит
   * @return Список из 4-х возможных предложений по кредиту
   */
  public List<LoanOfferDto> getOfferList(LoanStatementRequestDto request) {
    log.info("Инициирован расчёт кредитных предложений пользователя с e-mail: {}",
        request.getEmail());

    List<LoanOfferDto> offerList = new ArrayList<>(List.of(
        calculateOffer(request, false, false),
        calculateOffer(request, true, false),
        calculateOffer(request, false, true),
        calculateOffer(request, true, true)));
    offerList.sort(Comparator.comparing(LoanOfferDto::getRate).reversed());

    log.info("Завершён расчёт кредитных предложений с e-mail: {}", request.getEmail());
    return offerList;

  }

  /**
   * Рассчитывает ежемесячный аннуитетный платёж по кредиту.
   *
   * @param offerRate            Ставка кредитного предложения
   * @param offerRequestedAmount Запрошенная сумма кредита
   * @param term                 Срок кредита
   * @return Ежемесячный аннуитетный платёж по кредиту
   */
  public BigDecimal calculateMonthlyPayment(BigDecimal offerRate, BigDecimal offerRequestedAmount,
      int term) {
    BigDecimal monthRate = offerRate
        .divide(BigDecimal.valueOf(12), 8, RoundingMode.HALF_EVEN);
    BigDecimal addition = (monthRate.add(BigDecimal.valueOf(1))).pow(term);

    BigDecimal coefficient = monthRate
        .multiply(addition)
        .divide(addition.subtract(BigDecimal.valueOf(1)), 8, RoundingMode.HALF_EVEN);

    return offerRequestedAmount.multiply(coefficient).setScale(2, RoundingMode.HALF_EVEN);
  }

  /**
   * Рассчитывает полную стоимость кредита.
   *
   * @param requestedAmount Запрошенная сумма кредита
   * @param monthlyPayment  Ежемесячный аннуитетный платёж по кредиту
   * @param rate            Процентная ставка по кредиту
   * @return Полная стоимость кредита
   */
  public BigDecimal calculatePsk(BigDecimal requestedAmount, BigDecimal monthlyPayment,
      BigDecimal rate) {
    ZoneId z = ZoneId.of("Europe/Moscow");
    LocalDate date = LocalDate.now(z);
    BigDecimal remainingDebt = requestedAmount;
    BigDecimal interestPayment;
    BigDecimal debtPayment;
    BigDecimal totalPayment = monthlyPayment;
    BigDecimal paymentSum = new BigDecimal("0");

    while (remainingDebt.compareTo(BigDecimal.valueOf(0)) != 0) {
      interestPayment = remainingDebt.multiply(rate)
          .multiply(BigDecimal.valueOf(date.lengthOfMonth()))
          .divide(BigDecimal.valueOf(date.lengthOfYear()), RoundingMode.HALF_EVEN);
      date = date.plusMonths(1);
      if (remainingDebt.compareTo(monthlyPayment) < 0) {
        debtPayment = remainingDebt;
        totalPayment = debtPayment.add(interestPayment);
        remainingDebt = BigDecimal.valueOf(0);
      } else {
        debtPayment = monthlyPayment.subtract(interestPayment);
        remainingDebt = remainingDebt.subtract(debtPayment);
      }
      paymentSum = paymentSum.add(totalPayment);
    }
    return paymentSum.setScale(2, RoundingMode.HALF_EVEN);
  }


  private LoanOfferDto calculateOffer(LoanStatementRequestDto loanStatementRequestDto,
      boolean isInsuranceEnabled, boolean isSalaryClient) {
    BigDecimal offerRate = rate;
    BigDecimal offerRequestedAmount = loanStatementRequestDto.getAmount();
    int term = loanStatementRequestDto.getTerm();

    if (isInsuranceEnabled) {
      offerRate = offerRate.subtract(BigDecimal.valueOf(0.03));
      offerRequestedAmount = offerRequestedAmount.multiply(BigDecimal.valueOf(1.05));
    }
    if (isSalaryClient) {
      offerRate = offerRate.subtract(BigDecimal.valueOf(0.01));
    }
    log.debug(
        "Расчёт условий кредитного предложения: сумма кредита {}, ставка {}, срок {} мес.",
        offerRequestedAmount, offerRate, term);
    BigDecimal monthlyPayment = calculateMonthlyPayment(offerRate, offerRequestedAmount, term);
    log.debug("Ежемесячный платёж рассчитан: {}", monthlyPayment);
    BigDecimal totalAmount = calculatePsk(offerRequestedAmount, monthlyPayment, offerRate);
    log.debug("Полная стоимость кредита рассчитана: {}", totalAmount);
    monthlyPayment = monthlyPayment.setScale(2, RoundingMode.HALF_EVEN);

    return LoanOfferDto.builder()
        .rate(offerRate)
        .requestedAmount(offerRequestedAmount)
        .monthlyPayment(monthlyPayment)
        .totalAmount(totalAmount)
        .term(term)
        .isInsuranceEnabled(isInsuranceEnabled)
        .isSalaryClient(isSalaryClient)
        .build();
  }

}
