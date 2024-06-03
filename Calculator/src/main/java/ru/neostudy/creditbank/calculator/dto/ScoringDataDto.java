package ru.neostudy.creditbank.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neostudy.creditbank.calculator.enums.Gender;
import ru.neostudy.creditbank.calculator.enums.MaritalStatus;

@NoArgsConstructor
@Data
@Schema(description = "Данные, необходимые для скоринга")
public class ScoringDataDto {

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
  @Schema(description = "Пол клиента")
  private Gender gender;
  @Past(message = "Дата рождения указана некорректно.")
  @Schema(description = "Дата рождения клиента")
  private LocalDate birthdate;
  @Pattern(regexp = "\\d{4}", message = "Серия паспорта должна содержать 4 цифры.")
  @Schema(description = "Серия паспорта клиента")
  private String passportSeries;
  @Pattern(regexp = "\\d{6}", message = "Номер паспорта должен содержать 6 цифр.")
  @Schema(description = "Номер паспорта клиента")
  private String passportNumber;
  @PastOrPresent
  @Schema(description = "Дата выдачи паспорта клиента")
  private LocalDate passportIssueDate;
  @Pattern(regexp = "\\d{3}-\\d{3}",
      message = "Код подразделения состоит из 6 цифр в формате XXX-XXX.")
  @Schema(description = "Код подразделения, где был выдан паспорт клиента",
      example = "560-400")
  private String passportIssueBranch;
  @Schema(description = "Семейное положение клиента")
  private MaritalStatus maritalStatus;
  @Schema(description = "Количество иждивенцев на попечении клиента")
  private Integer dependentAmount;
  @NotNull
  @Schema(description = "Данные о работе клиента")
  private EmploymentDto employmentDto;
  @NotNull
  @Schema(description = "Номер счёта клиента")
  private String accountNumber;
  @Schema(description = "Наличие страховки по кредиту")
  private Boolean isInsuranceEnabled;
  @Schema(description = "Наличие статуса зарплатного клиента")
  private Boolean isSalaryClient;
}
