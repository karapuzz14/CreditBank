package ru.neostudy.creditbank.statement.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.neostudy.creditbank.statement.exception.DefaultException;
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
        "birthdate",
        message,
        request.getDescription(false)),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> onInvalidFormatException(HttpMessageNotReadableException e,
      WebRequest request) {
    var cause = e.getCause();

    if (cause instanceof InvalidFormatException) {
      cause = cause.getCause();

      if (cause instanceof DateTimeParseException) {
        return new ResponseEntity<>(new ErrorResponse(
            LocalDateTime.now(),
            "birthdate",
            "Некорректный ввод даты: '" + ((DateTimeParseException) cause).getParsedString()
                + "'. Корректный пример: 1990-09-20.",
            request.getDescription(false)),
            HttpStatus.BAD_REQUEST);
      }
    }

    return new ResponseEntity<>(new ErrorResponse(
        LocalDateTime.now(),
        "default",
        e.getMessage(),
        request.getDescription(false)),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> onMethodArgumentNotValidException(
      MethodArgumentNotValidException e, WebRequest request) {
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
