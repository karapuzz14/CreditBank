package ru.neostudy.creditbank.deal.interfaces;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.neostudy.creditbank.deal.config.FeignConfig;
import ru.neostudy.creditbank.deal.dto.CreditDto;
import ru.neostudy.creditbank.deal.dto.LoanOfferDto;
import ru.neostudy.creditbank.deal.dto.LoanStatementRequestDto;
import ru.neostudy.creditbank.deal.dto.ScoringDataDto;
import ru.neostudy.creditbank.deal.exception.CustomErrorDecoder;

@FeignClient(value = "calculator", configuration = {FeignConfig.class, CustomErrorDecoder.class})
public interface CalculatorClient {

  @RequestMapping(method = RequestMethod.POST, value = "/offers", consumes = "application/json")
  List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto);

  @RequestMapping(method = RequestMethod.POST, value = "/calc", consumes = "application/json")
  CreditDto getCredit(ScoringDataDto scoringData);
}