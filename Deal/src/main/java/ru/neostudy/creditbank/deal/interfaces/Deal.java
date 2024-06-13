package ru.neostudy.creditbank.deal.interfaces;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neostudy.creditbank.deal.dto.FinishRegistrationRequestDto;
import ru.neostudy.creditbank.deal.dto.LoanOfferDto;
import ru.neostudy.creditbank.deal.dto.LoanStatementRequestDto;

public interface Deal {

  List<LoanOfferDto> createLoanOffers(
      @Valid @RequestBody @Parameter(description = "Заявка на кредит", required = true)
      LoanStatementRequestDto loanStatementRequestDto);

  void selectOffer(
      @Valid @RequestBody @Parameter(description = "Выбранное кредитное предложение", required = true)
      LoanOfferDto loanOfferDto);

  void finishRegistration(
      @Valid @RequestBody @Parameter(description = "Запрос на расчёт выбранного кредитного предложения", required = true)
      FinishRegistrationRequestDto finishRegistrationRequestDto,
      String statementId);


}
