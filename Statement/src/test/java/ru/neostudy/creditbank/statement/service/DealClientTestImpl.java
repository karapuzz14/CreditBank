package ru.neostudy.creditbank.statement.service;

import java.math.BigDecimal;
import java.util.List;
import ru.neostudy.creditbank.statement.dto.LoanOfferDto;
import ru.neostudy.creditbank.statement.dto.LoanStatementRequestDto;
import ru.neostudy.creditbank.statement.interfaces.DealClient;

public class DealClientTestImpl implements DealClient {

  private List<LoanOfferDto> getLoanOffers() {
    LoanOfferDto example = LoanOfferDto.builder()
        .requestedAmount(new BigDecimal("100000"))
        .totalAmount(new BigDecimal("113636.81"))
        .term(20)
        .monthlyPayment(new BigDecimal("5682.04"))
        .rate(new BigDecimal("0.15"))
        .isInsuranceEnabled(false)
        .isSalaryClient(false)
        .build();
    return List.of(example, example, example, example);
  }

  @Override
  public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
    return getLoanOffers();
  }

  @Override
  public void selectOffer(LoanOfferDto loanOfferDto) {}
}
