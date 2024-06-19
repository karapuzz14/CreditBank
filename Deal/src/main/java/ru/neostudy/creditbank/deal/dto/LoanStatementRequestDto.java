package ru.neostudy.creditbank.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

  @NotNull
  @DecimalMax(value = "30000000", message = "Сумма кредита не может превышать 30 млн. рублей.")
  @DecimalMin(value = "30000", message = "Сумма кредита не может быть ниже 30 тыс. рублей.")
  @Schema(description = "Сумма кредита", required = true, example = "100000.00")
  private BigDecimal amount;

  @NotNull
  @Max(value = 84, message = "Максимальный срок кредита - 7 лет.")
  @Min(value = 6, message = "Минимальный срок кредита - 6 месяцев.")
  @Schema(description = "Срок кредита (мес.)", required = true, example = "24")
  private Integer term;

  @NotNull
  @Size(min = 2, max = 30, message = "Длина имени: от 2 до 30 знаков.")
  @Schema(description = "Имя клиента", required = true, example = "Владимир")
  private String firstName;

  @NotNull
  @Size(min = 2, max = 30, message = "Длина фамилии: от 2 до 30 знаков.")
  @Schema(description = "Фамилия клиента", required = true, example = "Краснов")
  private String lastName;

  @Schema(description = "Отчество клиента")
  private String middleName;

  @NotNull
  @Email
  @Schema(description = "Адрес электронной почты клиента", required = true, example = "testmail@yandex.ru")
  private String email;

  @NotNull
  @Past
  @Schema(description = "Дата рождения клиента", required = true, example = "1990-09-20")
  private LocalDate birthdate;

  @NotNull
  @Pattern(regexp = "\\d{4}", message = "Серия паспорта должна содержать 4 цифры.")
  @Schema(description = "Серия паспорта клиента", required = true, example = "2024")
  private String passportSeries;

  @NotNull
  @Pattern(regexp = "\\d{6}", message = "Номер паспорта должен содержать 6 цифр.")
  @Schema(description = "Номер паспорта клиента", required = true, example = "000001")
  private String passportNumber;
}
