package ru.neostudy.creditbank.statement.exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Возникает в случае, если пользователь несовершеннолетний.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LaterBirthdateException extends Exception {

  private String message;
  private LocalDateTime timestamp;
  private String details;
}
