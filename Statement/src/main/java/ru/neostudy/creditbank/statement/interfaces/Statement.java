package ru.neostudy.creditbank.statement.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neostudy.creditbank.statement.dto.LoanOfferDto;
import ru.neostudy.creditbank.statement.dto.LoanStatementRequestDto;
import ru.neostudy.creditbank.statement.exception.LaterBirthdateException;

public interface Statement {

  @Operation(
      summary = "Прескоринг и расчёт возможных условий кредита",
      description = "Осуществляет прескоринг и расчёт 4-х вариантов возможных условий кредита через Deal API"
          + " в зависимости от наличия страховки кредита и наличия у пользователя статуса зарплатного клиента."
  )
  List<LoanOfferDto> calculateLoanOffers(
      @Valid @RequestBody @Parameter(description = "Заявка на кредит", required = true)
      LoanStatementRequestDto loanStatementRequestDto)
      throws LaterBirthdateException;

  @Operation(
      summary = "Выбор кредитного предложения",
      description = "Осуществляет выбор кредитного предложения через Deal API."
  )
  void selectOffer(
      @Valid @RequestBody @Parameter(description = "Выбранное кредитное предложение", required = true)
      LoanOfferDto loanOfferDto);
}
