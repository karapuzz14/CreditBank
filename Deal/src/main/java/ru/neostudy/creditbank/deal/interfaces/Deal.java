package ru.neostudy.creditbank.deal.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neostudy.creditbank.deal.dto.FinishRegistrationRequestDto;
import ru.neostudy.creditbank.deal.dto.LoanOfferDto;
import ru.neostudy.creditbank.deal.dto.LoanStatementRequestDto;

public interface Deal {

  @Operation(
      summary = "Обработка заявки на кредит и возврат 4-х кредитных предложений.",
      description =
          "Сохраняет в БД основные данные о клиенте и связанную с ним пустую заявку на кредит, "
              + "возвращает 4 возможных варианта условий кредита от худшего к лучшему."
  )
  List<LoanOfferDto> createLoanOffers(
      @Valid @RequestBody @Parameter(description = "Запрос на создание заявки на кредит", required = true)
      LoanStatementRequestDto loanStatementRequestDto);

  @Operation(
      summary = "Сохранение данных о выбранном кредитном предложении",
      description = "Сохраняет данные о выбранном кредитном предложении в поле заявки на кредит."
  )
  void selectOffer(
      @Valid @RequestBody @Parameter(description = "Выбранное кредитное предложение", required = true)
      LoanOfferDto loanOfferDto);

  @Operation(
      summary = "Сохранение итоговых вычисленные данных о кредитном предложении",
      description = "Сохраняет в БД полные данные о клиенте и вычисленные данные итогового кредитного предложения."
  )
  void finishRegistration(
      @Valid @RequestBody @Parameter(description = "Запрос на расчёт выбранного кредитного предложения", required = true)
      FinishRegistrationRequestDto finishRegistrationRequestDto,
      String statementId);


}
