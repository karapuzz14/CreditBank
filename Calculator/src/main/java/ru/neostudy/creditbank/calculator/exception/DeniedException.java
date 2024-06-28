package ru.neostudy.creditbank.calculator.exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Возникает в случае отказа после скоринга.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeniedException extends Exception {

  private String message;
  private LocalDateTime timestamp;
  private String details;

}
