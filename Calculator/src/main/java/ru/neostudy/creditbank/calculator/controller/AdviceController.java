package ru.neostudy.creditbank.calculator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.neostudy.creditbank.calculator.exception.LaterBirthdateException;

/**
 * Глобальный контроллер для перехвата ошибок.
 */
@RestControllerAdvice
@Slf4j
public class AdviceController {

  @ExceptionHandler(LaterBirthdateException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public LaterBirthdateException onLaterBirthdateException(LaterBirthdateException e) {
    log.error(e.getMessage());
    return e;
  }
}
