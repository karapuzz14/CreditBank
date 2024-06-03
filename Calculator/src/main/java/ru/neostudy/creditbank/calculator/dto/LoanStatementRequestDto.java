package ru.neostudy.creditbank.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Schema(description = "Запрос на кредит")
public class LoanStatementRequestDto {

  @DecimalMax(value = "30000000", message = "Сумма кредита не может превышать 30 млн. рублей.")
  @DecimalMin(value = "30000", message = "Сумма кредита не может быть ниже 30 тыс. рублей.")
  @Schema(description = "Сумма кредита")
  private BigDecimal amount;
  @Max(value = 84, message = "Максимальный срок кредита - 7 лет.")
  @Min(value = 6, message = "Минимальный срок кредита - 6 месяцев.")
  @Schema(description = "Срок кредита (мес.)")
  private Integer term;
  @Size(min = 2, max = 30, message = "Длина имени: от 2 до 30 знаков.")
  @Schema(description = "Имя клиента")
  private String firstName;
  @Size(min = 2, max = 30, message = "Длина фамилии: от 2 до 30 знаков.")
  @Schema(description = "Фамилия клиента")
  private String lastName;
  @Schema(description = "Отчество клиента")
  private String middleName;
  @Email
  @Schema(description = "Адрес электронной почты клиента")
  private String email;
  @Past
  @Schema(description = "Дата рождения клиента")
  private LocalDate birthdate;
  @Pattern(regexp = "\\d{4}", message = "Серия паспорта должна содержать 4 цифры.")
  @Schema(description = "Серия паспорта клиента")
  private String passportSeries;
  @Pattern(regexp = "\\d{6}", message = "Номер паспорта должен содержать 6 цифр.")
  @Schema(description = "Номер паспорта клиента")
  private String passportNumber;
}
