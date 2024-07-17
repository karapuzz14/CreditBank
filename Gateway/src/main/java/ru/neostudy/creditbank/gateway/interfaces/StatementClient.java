package ru.neostudy.creditbank.gateway.interfaces;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import ru.neostudy.creditbank.gateway.config.FeignConfig;
import ru.neostudy.creditbank.gateway.dto.LoanOfferDto;
import ru.neostudy.creditbank.gateway.dto.LoanStatementRequestDto;
import ru.neostudy.creditbank.gateway.exception.CustomErrorDecoder;
import ru.neostudy.creditbank.gateway.exception.DefaultException;

@FeignClient(value = "statement", configuration = {FeignConfig.class, CustomErrorDecoder.class})
public interface StatementClient {

  @PostMapping("")
  List<LoanOfferDto> createLoanOffers(LoanStatementRequestDto loanStatementRequestDto)
      throws DefaultException;

  @PostMapping("/offer")
  void selectOffer(LoanOfferDto loanOfferDto) throws DefaultException;
}
