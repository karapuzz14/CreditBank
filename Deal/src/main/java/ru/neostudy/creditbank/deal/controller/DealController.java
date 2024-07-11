package ru.neostudy.creditbank.deal.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neostudy.creditbank.deal.dto.FinishRegistrationRequestDto;
import ru.neostudy.creditbank.deal.dto.LoanOfferDto;
import ru.neostudy.creditbank.deal.dto.LoanStatementRequestDto;
import ru.neostudy.creditbank.deal.exception.DefaultException;
import ru.neostudy.creditbank.deal.exception.DeniedException;
import ru.neostudy.creditbank.deal.interfaces.Deal;
import ru.neostudy.creditbank.deal.service.DealService;
import ru.neostudy.creditbank.deal.service.EmailService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/deal")
public class DealController implements Deal {

  private final DealService dealService;
  private final EmailService emailService;

  @PostMapping("/statement")
  public List<LoanOfferDto> createLoanOffers(LoanStatementRequestDto statementRequest)
      throws DefaultException {
    log.debug("Запрос на обработку кредитной заявки: {}", statementRequest.toString());

    List<LoanOfferDto> result = dealService.createStatement(statementRequest);

    log.debug("Ответ после обработки кредитной заявки: {}", result.toString());
    return result;


  }

  @PostMapping("/offer/select")
  public void selectOffer(LoanOfferDto loanOfferDto) {
    log.debug("Выбор кредитного предложения: {}", loanOfferDto.toString());

    dealService.selectOffer(loanOfferDto);
  }

  @PostMapping("/calculate/{statementId}")
  public void finishRegistration(FinishRegistrationRequestDto finishRequest,
      @PathVariable String statementId) throws DeniedException, DefaultException {
    log.debug("Запрос на расчёт кредитного предложения по заявке {}: {}",
        statementId, finishRequest.toString());

    dealService.createCredit(finishRequest, statementId);
  }

  @PostMapping("/document/{statementId}/send")
  public void sendDocuments(@PathVariable String statementId) {
    log.debug("Запрос на формирование и отправку документов по заявке {}", statementId);

    emailService.sendEmailMessage("send-documents", statementId);
  }

  @PostMapping("/document/{statementId}/sign")
  public void signDocuments(@PathVariable String statementId) {
    log.debug("Запрос на подписание документов по заявке {}", statementId);

    emailService.sendEmailMessage("send-ses", statementId);
  }

  @PostMapping("/document/{statementId}/code")
  public void sendCode(@PathVariable String statementId) {
    log.debug("Запрос на подтверждение кода для подписания документов по заявке {}", statementId);

    emailService.sendEmailMessage("credit_issued", statementId);
  }
}
