package ru.neostudy.creditbank.calculator.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neostudy.creditbank.calculator.dto.CreditDto;
import ru.neostudy.creditbank.calculator.dto.LoanOfferDto;
import ru.neostudy.creditbank.calculator.dto.LoanStatementRequestDto;
import ru.neostudy.creditbank.calculator.dto.ScoringDataDto;
import ru.neostudy.creditbank.calculator.exception.DeniedException;
import ru.neostudy.creditbank.calculator.exception.LaterBirthdateException;
import ru.neostudy.creditbank.calculator.interfaces.Calculate;
import ru.neostudy.creditbank.calculator.service.CreditService;
import ru.neostudy.creditbank.calculator.service.OfferService;

/**
 * Основной контроллер Calculator API. Рассчитывает возможные и полные условия кредита.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/calculator")
@Tag(name = "Калькулятор", description = "Рассчитывает возможные и полные условия кредита.")
public class CalculatorController implements Calculate {

  private final CreditService creditService;
  private final OfferService offerService;

  /**
   * Рассчитывает возможные условия кредита в зависимости от наличия страховки и статуса зарплатного
   * клиента.
   *
   * @param loanStatementRequestDto Заявка на кредит
   * @return Список 4-х возможных условий кредита
   * @throws LaterBirthdateException Ошибка - несовершеннолетний пользователь
   */
  @PostMapping("/offers")
  public List<LoanOfferDto> calculateLoanOffers(
      LoanStatementRequestDto loanStatementRequestDto)
      throws LaterBirthdateException {
    log.debug("Запрос на расчёт возможных условий кредита: {}", loanStatementRequestDto.toString());

    offerService.isDateLate(loanStatementRequestDto.getBirthdate());
    List<LoanOfferDto> result = offerService.getOfferList(loanStatementRequestDto);

    log.debug("Ответ после расчёта возможных условий кредита: {}", result.toString());
    return result;
  }

  /**
   * Рассчитывает полные условия кредита.
   *
   * @param scoringDataDto Персональные данные для скоринга
   * @return Полные условия кредита
   */
  @PostMapping("/calc")
  public CreditDto calculateCreditOffer(ScoringDataDto scoringDataDto) throws DeniedException {
    log.debug("Запрос на расчёт полных условий кредита: {}", scoringDataDto.toString());

    CreditDto result = creditService.calculateCredit(scoringDataDto);

    log.debug("Ответ после расчёта полных условий кредита: {}", result.toString());
    return result;
  }

}
