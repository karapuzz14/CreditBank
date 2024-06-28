package ru.neostudy.creditbank.statement.interfaces;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.neostudy.creditbank.statement.config.FeignConfig;
import ru.neostudy.creditbank.statement.dto.LoanOfferDto;
import ru.neostudy.creditbank.statement.dto.LoanStatementRequestDto;
import ru.neostudy.creditbank.statement.exception.CustomErrorDecoder;
import ru.neostudy.creditbank.statement.exception.DefaultException;

@FeignClient(value = "deal", configuration = {FeignConfig.class, CustomErrorDecoder.class})
@Component
public interface DealClient {

  @RequestMapping(method = RequestMethod.POST, value = "/statement", consumes = "application/json")
  List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) throws DefaultException;

  @RequestMapping(method = RequestMethod.POST, value = "/offer/select", consumes = "application/json")
  void selectOffer(LoanOfferDto loanOfferDto) throws DefaultException;
}