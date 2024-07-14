package ru.neostudy.creditbank.deal.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neostudy.creditbank.deal.dto.FinishRegistrationRequestDto;
import ru.neostudy.creditbank.deal.dto.LoanOfferDto;
import ru.neostudy.creditbank.deal.dto.LoanStatementRequestDto;
import ru.neostudy.creditbank.deal.exception.DefaultException;
import ru.neostudy.creditbank.deal.exception.DeniedException;

public interface Deal {

  @Operation(
      summary = "Обработка заявки на кредит и возврат 4-х кредитных предложений.",
      description =
          "Сохраняет в БД основные данные о клиенте и связанную с ним пустую заявку на кредит, "
              + "возвращает 4 возможных варианта условий кредита от худшего к лучшему."
  )
  List<LoanOfferDto> createLoanOffers(
      @Valid @RequestBody @Parameter(description = "Запрос на создание заявки на кредит", required = true)
      LoanStatementRequestDto loanStatementRequestDto) throws DefaultException;

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
      String statementId) throws DeniedException, DefaultException;

  @Operation(
      summary = "Формирование и отправка документов",
      description = "Формирует текст документов и отправляет его в МС-Dossier "
          + "для последующей отправки письма с документами и ссылкой на их подписание. "
          + "Изменяет статус заявки на PREPARE_DOCUMENTS."
  )
  void sendDocuments(
      @Parameter(description = "Идентификатор заявки", required = true) String statementId);

  @Operation(
      summary = "Обновление статуса заявки",
      description = "Изменяет статус заявки на DOCUMENTS_CREATED после отправки письма с документами в МС-Dossier."
  )
  void changeStatusOnDocumentsCreated(
      @Parameter(description = "Идентификатор заявки", required = true) String statementId);

  @Operation(
      summary = "Подписание документов",
      description = "Обрабатывает решение пользователя по условиям кредита (согласие/отказ), "
          + "в случае согласия отправляет на почту ссылку на подтверждение подписи с 4-хзначным кодом. "
          + "В случае отказа изменяет в БД статус заявки на CLIENT_DENIED. "
          + "Сохраняет в БД код подтверждения для соответствующей заявки."
  )
  void signDocuments(
      @Parameter(description = "Согласие/отказ клиента в подписании документов", required = true) Boolean isAccepted,
      @Parameter(description = "Идентификатор заявки", required = true) String statementId);


  @Operation(
      summary = "Подтверждение подписания документов",
      description = "Сравнивает полученный и отправленный код. "
          + "В случае совпадения изменяет в БД статус заявки на CREDIT_ISSUED, статус кредита на ISSUED. "
          + "Отправляет уведомление о выдаче кредита на почту."
  )
  void sendCodeVerification(
      @Parameter(description = "Код подтверждения") String code,
      @Parameter(description = "Идентификатор заявки", required = true) String statementId);

}
