package ru.neostudy.creditbank.calculator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.neostudy.creditbank.calculator.exception.DeniedException;
import ru.neostudy.creditbank.calculator.exception.ErrorResponse;

/**
 * Глобальный контроллер для перехвата ошибок.
 */
@RestControllerAdvice
@Slf4j
public class AdviceController {

  @ExceptionHandler(DeniedException.class)
  public ResponseEntity<ErrorResponse> onDeniedException(DeniedException e, WebRequest request) {
    return new ResponseEntity<>(new ErrorResponse(
        e.getTimestamp(),
        "cc_denied",
        e.getMessage(),
        request.getDescription(false)),
        HttpStatus.BAD_REQUEST);
  }
}
