package ru.neostudy.creditbank.deal.controller;

import java.util.List;
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
@RequestMapping("/deal")
public class DealController implements Deal {

  private final DealService dealService;

  public DealController(DealService dealService) {
    this.dealService = dealService;
  }

  @PostMapping("/statement")
  public List<LoanOfferDto> createLoanOffers(LoanStatementRequestDto statementRequest) {
    List<LoanOfferDto> offers = dealService.createStatement(statementRequest);
    System.out.println(offers);
    return offers;
  }

  @PostMapping("/offer/select")
  public void selectOffer(LoanOfferDto loanOfferDto) {
    dealService.selectOffer(loanOfferDto);
  }

  @PostMapping("/calculate/{statementId}")
  public void finishRegistration(FinishRegistrationRequestDto finishRequest,
      @PathVariable String statementId) {
    dealService.createCredit(finishRequest, statementId);
  }
}
