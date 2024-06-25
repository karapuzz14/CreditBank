package ru.neostudy.creditbank.calculator.controller;

import java.time.LocalDateTime;
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

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> onNativeDefaultException(Exception e, WebRequest request) {
    log.error(e.getMessage());
    return new ResponseEntity<>(new ErrorResponse(
        LocalDateTime.now(),
        "default",
        e.getMessage(),
        request.getDescription(false)),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
