package ru.neostudy.creditbank.gateway.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neostudy.creditbank.gateway.dto.FinishRegistrationRequestDto;
import ru.neostudy.creditbank.gateway.dto.LoanOfferDto;
import ru.neostudy.creditbank.gateway.dto.LoanStatementRequestDto;
import ru.neostudy.creditbank.gateway.dto.entity.Statement;
import ru.neostudy.creditbank.gateway.exception.DefaultException;

public interface Gateway {

  @Operation(
      summary = "Прескоринг и расчёт возможных условий кредита",
      description = "Осуществляет прескоринг и расчёт 4-х вариантов возможных условий кредита через Statement API"
          + " в зависимости от наличия страховки кредита и наличия у пользователя статуса зарплатного клиента."
  )
  List<LoanOfferDto> calculateLoanOffers(
      @RequestBody @Parameter(description = "Заявка на кредит", required = true)
      LoanStatementRequestDto loanStatementRequestDto)
      throws DefaultException;

  @Operation(
      summary = "Выбор кредитного предложения",
      description = "Осуществляет выбор кредитного предложения через Statement API."
  )
  void selectOffer(
      @RequestBody @Parameter(description = "Выбранное кредитное предложение", required = true)
      LoanOfferDto loanOfferDto) throws DefaultException;

  @Operation(
      summary = "Сохранение итоговых вычисленные данных о кредитном предложении",
      description = "Сохраняет в БД полные данные о клиенте и вычисленные данные итогового кредитного предложения через Deal API."
  )
  void finishRegistration(
      @RequestBody @Parameter(description = "Запрос на расчёт выбранного кредитного предложения", required = true)
      FinishRegistrationRequestDto finishRegistrationRequestDto,
      String statementId) throws DefaultException;

  @Operation(
      summary = "Формирование и отправка документов",
      description = "Формирует и отправляет документы через Deal API."
          + "Изменяет статус заявки на PREPARE_DOCUMENTS."
  )
  void sendDocuments(
      @Parameter(description = "Идентификатор заявки", required = true) String statementId)
      throws DefaultException;

  @Operation(
      summary = "Подписание документов",
      description = "Отправляет запрос на обработку решения пользователя по условиям кредита (согласие/отказ) в Deal API. "
          + "В случае согласия отправляет на почту ссылку на подтверждение подписи с 4-хзначным кодом. "
          + "В случае отказа изменяет в БД статус заявки на CLIENT_DENIED. "
          + "Сохраняет в БД код подтверждения для соответствующей заявки."
  )
  void signDocuments(
      @Parameter(description = "Согласие/отказ клиента в подписании документов", required = true) Boolean isAccepted,
      @Parameter(description = "Идентификатор заявки", required = true) String statementId)
      throws DefaultException;

  @Operation(
      summary = "Подтверждение подписания документов",
      description = "Сравнивает полученный и отправленный код подтверждения подписи через Deal API. "
          + "В случае совпадения изменяет в БД статус заявки на CREDIT_ISSUED, статус кредита на ISSUED. "
          + "Отправляет уведомление о выдаче кредита на почту."
  )
  void sendCodeVerification(
      @Parameter(description = "Код подтверждения") String code,
      @Parameter(description = "Идентификатор заявки", required = true) String statementId)
      throws DefaultException;

  @Operation(
      summary = "Получение заявки по ID",
      description = "Возвращает заявку по её идентификатору. Функция администратора."
  )
  Statement getStatementById(
      @Parameter(description = "Идентификатор заявки", required = true) String statementId)
      throws DefaultException;

  @Operation(
      summary = "Получение всех заявок",
      description = "Возвращает все заявки. Функция администратора."
  )
  List<Statement> getAllStatements() throws DefaultException;
}
