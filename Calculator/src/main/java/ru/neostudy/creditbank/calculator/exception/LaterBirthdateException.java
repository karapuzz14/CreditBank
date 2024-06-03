package ru.neostudy.creditbank.calculator.exception;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Возникает в случае, если пользователь несовершеннолетний.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LaterBirthdateException extends Exception {

  private String message;
  private Date timestamp;

}
