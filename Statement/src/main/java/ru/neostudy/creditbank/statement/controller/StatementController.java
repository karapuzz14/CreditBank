package ru.neostudy.creditbank.statement.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neostudy.creditbank.statement.dto.LoanOfferDto;
import ru.neostudy.creditbank.statement.dto.LoanStatementRequestDto;
import ru.neostudy.creditbank.statement.exception.DefaultException;
import ru.neostudy.creditbank.statement.exception.LaterBirthdateException;
import ru.neostudy.creditbank.statement.interfaces.Statement;
import ru.neostudy.creditbank.statement.service.StatementService;

/**
 * Основной контроллер Statement API. Осуществляет прескоринг и выбор кредитного предложения.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/statement")
@Tag(name = "Заявка", description = "Осуществляет прескоринг и выбор кредитного предложения.")
public class StatementController implements Statement {

  private final StatementService statementService;

  /**
   * Производит прескоринг и рассчитывает возможные условия кредита в зависимости от наличия страховки и статуса зарплатного
   * клиента.
   *
   * @param loanStatementRequestDto Заявка на кредит
   * @return Список 4-х возможных условий кредита
   * @throws LaterBirthdateException Ошибка - несовершеннолетний пользователь
   */
  @PostMapping("")
  public List<LoanOfferDto> calculateLoanOffers(
      LoanStatementRequestDto loanStatementRequestDto)
      throws LaterBirthdateException, DefaultException {
    log.debug("Запрос на расчёт возможных условий кредита: {}", loanStatementRequestDto.toString());

    List<LoanOfferDto> offers = statementService.getOffers(loanStatementRequestDto);

    log.debug("Ответ после расчёта возможных условий кредита: {}", offers.toString());
    return offers;
  }

  /**
   * Производит выбор кредитного предложения, отправляя выбранное предложение в Deal API.
   *
   * @param loanOfferDto Выбранное предложение
   */
  @PostMapping("/offer")
  public void selectOffer(LoanOfferDto loanOfferDto) throws DefaultException {
    log.debug("Выбор кредитного предложения: {}", loanOfferDto.toString());

    statementService.selectOffer(loanOfferDto);
  }

}
