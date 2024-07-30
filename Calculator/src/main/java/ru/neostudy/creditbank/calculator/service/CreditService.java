package ru.neostudy.creditbank.calculator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.neostudy.creditbank.calculator.dto.CreditDto;
import ru.neostudy.creditbank.calculator.dto.EmploymentDto;
import ru.neostudy.creditbank.calculator.dto.PaymentScheduleElementDto;
import ru.neostudy.creditbank.calculator.dto.ScoringDataDto;
import ru.neostudy.creditbank.calculator.enums.EmploymentStatus;
import ru.neostudy.creditbank.calculator.enums.Gender;
import ru.neostudy.creditbank.calculator.exception.DeniedException;

/**
 * Сервис, содержащий логику для расчёта полных условий кредита.
 */
@Service
@Slf4j
public class CreditService {

  @Value("${rate}")
  private BigDecimal rate;

  /**
   * Рассчитывает полные условия кредита.
   *
   * @param scoringDataDto Персональные данные для скоринга
   * @return Полные условия кредита, включая график ежемесячный платежей
   */
  public CreditDto calculateCredit(ScoringDataDto scoringDataDto) throws DeniedException {
    if (isDenied(scoringDataDto)) {
      log.error("Отказ в кредите пользователю с номером счёта {}",
          scoringDataDto.getAccountNumber());
      throw new DeniedException("Ошибка скоринга - отказано в кредите.", LocalDateTime.now(), "");
    }
    log.info("Инициирован расчёт полных условий кредита пользователя с номером счёта {}",
        scoringDataDto.getAccountNumber());

    int term = scoringDataDto.getTerm();
    BigDecimal requestedAmount = scoringDataDto.getAmount();
    BigDecimal scoredRate = calculateScoredRate(scoringDataDto);
    log.debug(
        "Расчёт условий кредита: сумма кредита {}, ставка {}, срок {} мес.",
        requestedAmount, scoredRate, term);

    OfferService offerService = new OfferService();
    BigDecimal monthlyPayment = offerService.calculateMonthlyPayment(scoredRate, requestedAmount, term);
    log.debug("Ежемесячный платёж рассчитан: {}", monthlyPayment);

    BigDecimal psk = offerService.calculatePsk(requestedAmount, monthlyPayment, scoredRate);
    log.debug("Полная стоимость кредита рассчитана: {}", psk);

    List<PaymentScheduleElementDto> schedule = createSchedule(requestedAmount, monthlyPayment,
        scoredRate);
    log.debug("Расписание платежей рассчитано за период {}/{}, ",
        schedule.get(0).getDate(),
        schedule.get(term - 1).getDate());

    log.info("Завершён расчёт полных условий кредита пользователя с номером счёта {}",
        scoringDataDto.getAccountNumber());
    return CreditDto.builder()
        .paymentSchedule(schedule)
        .monthlyPayment(monthlyPayment)
        .psk(psk)
        .rate(scoredRate)
        .term(term)
        .amount(scoringDataDto.getAmount())
        .isInsuranceEnabled(scoringDataDto.getIsInsuranceEnabled())
        .isSalaryClient(scoringDataDto.getIsSalaryClient())
        .build();
  }

  private List<PaymentScheduleElementDto> createSchedule(BigDecimal requestedAmount,
      BigDecimal monthlyPayment, BigDecimal rate) {
    ZoneId z = ZoneId.of("Europe/Moscow");
    LocalDate date = LocalDate.now(z);

    BigDecimal remainingDebt = requestedAmount;
    BigDecimal interestPayment;
    BigDecimal debtPayment;
    BigDecimal totalPayment = monthlyPayment;
    int num = 1;
    List<PaymentScheduleElementDto> schedule = new ArrayList<>();

    while (remainingDebt.compareTo(BigDecimal.valueOf(0)) != 0) {
      interestPayment = remainingDebt.multiply(rate)
          .multiply(BigDecimal.valueOf(date.lengthOfMonth()))
          .divide(BigDecimal.valueOf(date.lengthOfYear()), RoundingMode.HALF_EVEN);

      date = date.plusMonths(1);
      if (remainingDebt.compareTo(monthlyPayment) < 0) {
        debtPayment = remainingDebt;
        totalPayment = debtPayment.add(interestPayment);
      } else {
        debtPayment = monthlyPayment.subtract(interestPayment);
      }
      remainingDebt = remainingDebt.subtract(debtPayment);

      schedule.add(
          PaymentScheduleElementDto.builder()
              .number(num)
              .date(date)
              .totalPayment(totalPayment.setScale(2, RoundingMode.HALF_EVEN))
              .interestPayment(interestPayment.setScale(2, RoundingMode.HALF_EVEN))
              .debtPayment(debtPayment.setScale(2, RoundingMode.HALF_EVEN))
              .remainingDebt(remainingDebt.setScale(2, RoundingMode.HALF_EVEN))
              .build()
      );
      ++num;
    }

    return schedule;
  }

  private BigDecimal calculateScoredRate(ScoringDataDto scoringDataDto) {
    BigDecimal creditRate = rate;
    EmploymentDto employmentDto = scoringDataDto.getEmploymentDto();

    switch (employmentDto.getEmploymentStatus()) {
      case EMPLOYED -> creditRate = creditRate.add(BigDecimal.valueOf(0.01));
      case SELF_EMPLOYED -> creditRate = creditRate.add(BigDecimal.valueOf(0.02));
      case EMPLOYER -> creditRate = creditRate.add(BigDecimal.valueOf(0.03));
    }

    switch (employmentDto.getPosition()) {
      case ORDINARY -> creditRate = creditRate.subtract(BigDecimal.valueOf(0.01));
      case LOWER_MANAGER -> creditRate = creditRate.subtract(BigDecimal.valueOf(0.02));
      case MIDDLE_MANAGER -> creditRate = creditRate.subtract(BigDecimal.valueOf(0.03));
      case TOP_MANAGER -> creditRate = creditRate.subtract(BigDecimal.valueOf(0.04));
    }

    switch (scoringDataDto.getMaritalStatus()) {
      case MARRIED -> creditRate = creditRate.subtract(BigDecimal.valueOf(0.03));
      case DIVORCED -> creditRate = creditRate.add(BigDecimal.valueOf(0.01));
    }

    int age = Period.between(scoringDataDto.getBirthdate(), LocalDate.now()).getYears();
    if (scoringDataDto.getGender() == Gender.FEMALE
        && age >= 32
        && age <= 60) {
      creditRate = creditRate.subtract(BigDecimal.valueOf(0.03));
    }
    if (scoringDataDto.getGender() == Gender.MALE
        && age >= 30
        && age <= 55) {
      creditRate = creditRate.subtract(BigDecimal.valueOf(0.03));
    }

    return creditRate;
  }

  private boolean isDenied(ScoringDataDto scoringDataDto) {
    int age = Period.between(scoringDataDto.getBirthdate(), LocalDate.now()).getYears();
    EmploymentDto employmentDto = scoringDataDto.getEmploymentDto();

    return employmentDto.getEmploymentStatus() == EmploymentStatus.UNEMPLOYED
        || employmentDto.getSalary().multiply(BigDecimal.valueOf(25))
        .compareTo(scoringDataDto.getAmount()) < 0
        || age < 20
        || age > 65
        || employmentDto.getWorkExperienceCurrent() < 3
        || employmentDto.getWorkExperienceTotal() < 18;
  }
}
