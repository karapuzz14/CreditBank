package ru.neostudy.creditbank.deal.exception;

import java.util.Date;
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

  private Date timestamp;

}
