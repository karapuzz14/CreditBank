package ru.neostudy.creditbank.statement.exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DefaultException extends Exception {

  private LocalDateTime timestamp;

  private String code;

  private String message;

  private String details;

}
