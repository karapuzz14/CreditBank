package ru.neostudy.creditbank.calculator.exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

  private LocalDateTime timestamp;

  private String code;

  private String message;

  private String details;
}
