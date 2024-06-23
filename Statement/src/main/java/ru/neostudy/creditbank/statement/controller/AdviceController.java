package ru.neostudy.creditbank.statement.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.neostudy.creditbank.statement.exception.ErrorResponse;
import ru.neostudy.creditbank.statement.exception.LaterBirthdateException;

/**
 * Глобальный контроллер для перехвата ошибок.
 */
@RestControllerAdvice
@Slf4j
public class AdviceController {

  @ExceptionHandler(LaterBirthdateException.class)
  public ResponseEntity<ErrorResponse> onLaterBirthdateException(LaterBirthdateException e,
      WebRequest request) {
    String message = e.getMessage();
    log.error(message);
    return new ResponseEntity<>(new ErrorResponse(
        e.getTimestamp(),
        "minor_user",
        message,
        request.getDescription(false)),
        HttpStatus.BAD_REQUEST);
  }

}
