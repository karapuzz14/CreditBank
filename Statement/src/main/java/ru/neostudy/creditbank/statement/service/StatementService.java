package ru.neostudy.creditbank.statement.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neostudy.creditbank.statement.dto.LoanOfferDto;
import ru.neostudy.creditbank.statement.dto.LoanStatementRequestDto;
import ru.neostudy.creditbank.statement.exception.LaterBirthdateException;
import ru.neostudy.creditbank.statement.interfaces.DealClient;

/**
 * Сервис для осуществления прескоринга и выбора кредитного предложения.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StatementService {

  private final DealClient dealClient;

  /**
   * Проверяет совершеннолетие пользователя.
   *
   * @param date Дата рождения пользователя
   * @throws LaterBirthdateException Ошибка - пользователь несовершеннолетний.
   */
  private void isDateLate(LocalDate date) throws LaterBirthdateException {
    LocalDate checkpoint = LocalDate.now().minusYears(18);
    if (date.isAfter(checkpoint)) {
      throw new LaterBirthdateException("Некорректно указана дата рождения: '" + date
          + "'. Пользователь несовершеннолетний.",
          new Date());
    }
  }

  /**
   * Отправляет запрос на расчёт 4-х кредитных предложений в Deal API.
   *
   * @param loanStatementRequestDto Запрос на обработку заявки.
   * @throws LaterBirthdateException Ошибка - пользователь несовершеннолетний.
   */
  public List<LoanOfferDto> getOffers(LoanStatementRequestDto loanStatementRequestDto)
      throws LaterBirthdateException {
    isDateLate(loanStatementRequestDto.getBirthdate());

    return dealClient.getLoanOffers(loanStatementRequestDto);
  }

  /**
   * Отправляет запрос на выбор указанного кредитного предложения в Deal API.
   *
   * @param loanOfferDto Выбранное кредитное предложение.
   */
  public void selectOffer(LoanOfferDto loanOfferDto) {
    dealClient.selectOffer(loanOfferDto);
  }

}
