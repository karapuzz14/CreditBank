package ru.neostudy.creditbank.gateway.controller;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.neostudy.creditbank.gateway.exception.DefaultException;
import ru.neostudy.creditbank.gateway.exception.ErrorResponse;

/**
 * Глобальный контроллер для перехвата ошибок.
 */
@RestControllerAdvice
@Slf4j
public class AdviceController {

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

  @ExceptionHandler(DefaultException.class)
  public ResponseEntity<ErrorResponse> onDefaultException(DefaultException e) {
    log.error(e.getMessage());
    return new ResponseEntity<>(new ErrorResponse(
        e.getTimestamp(),
        e.getCode(),
        e.getMessage(),
        e.getDetails()),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
