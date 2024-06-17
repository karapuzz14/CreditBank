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
import ru.neostudy.creditbank.deal.interfaces.Deal;
import ru.neostudy.creditbank.deal.service.DealService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/deal")
public class DealController implements Deal {

  private final DealService dealService;

  @PostMapping("/statement")
  public List<LoanOfferDto> createLoanOffers(LoanStatementRequestDto statementRequest) {
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
      @PathVariable String statementId) {
    log.debug("Запрос на расчёт кредитного предложения по заявке {}: {}",
        statementId, finishRequest.toString());

    dealService.createCredit(finishRequest, statementId);
  }
}