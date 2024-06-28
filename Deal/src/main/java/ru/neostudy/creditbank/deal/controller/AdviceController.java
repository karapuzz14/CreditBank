package ru.neostudy.creditbank.deal.controller;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.neostudy.creditbank.deal.exception.DefaultException;
import ru.neostudy.creditbank.deal.exception.DeniedException;
import ru.neostudy.creditbank.deal.exception.ErrorResponse;

/**
 * Глобальный контроллер для перехвата ошибок.
 */
@RestControllerAdvice
@Slf4j
public class AdviceController {


  @ExceptionHandler(DeniedException.class)
  public ResponseEntity<ErrorResponse> onDeniedException(DeniedException e) {
    log.warn(e.getMessage());
    return new ResponseEntity<>(new ErrorResponse(
        e.getTimestamp(),
        "cc_denied",
        e.getMessage(),
        e.getDetails()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> onMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
    log.error(e.getMessage());

    return new ResponseEntity<>(new ErrorResponse(
        LocalDateTime.now(),
        Objects.requireNonNull(e.getFieldError()).getField(),
        e.getFieldError().getDefaultMessage(),
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
