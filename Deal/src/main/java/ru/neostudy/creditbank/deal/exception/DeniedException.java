package ru.neostudy.creditbank.deal.exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Возникает в случае отказа после скоринга.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeniedException extends Exception {

  private String message;

  private LocalDateTime timestamp;

  private String details;
}
