package ru.neostudy.creditbank.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
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

  @NotNull
  @DecimalMax(value = "30000000", message = "Сумма кредита не может превышать 30 млн. рублей.")
  @DecimalMin(value = "30000", message = "Сумма кредита не может быть ниже 30 тыс. рублей.")
  @Schema(description = "Сумма кредита", requiredMode = Schema.RequiredMode.REQUIRED, example = "100000.00")
  private BigDecimal amount;

  @NotNull
  @Max(value = 84, message = "Максимальный срок кредита - 7 лет.")
  @Min(value = 6, message = "Минимальный срок кредита - 6 месяцев.")
  @Schema(description = "Срок кредита (мес.)", requiredMode = Schema.RequiredMode.REQUIRED, example = "24")
  private Integer term;

  @NotNull
  @Size(min = 2, max = 30, message = "Длина имени: от 2 до 30 знаков.")
  @Schema(description = "Имя клиента", requiredMode = Schema.RequiredMode.REQUIRED, example = "Владимир")
  private String firstName;

  @NotNull
  @Size(min = 2, max = 30, message = "Длина фамилии: от 2 до 30 знаков.")
  @Schema(description = "Фамилия клиента", requiredMode = Schema.RequiredMode.REQUIRED, example = "Краснов")
  private String lastName;

  @Schema(description = "Отчество клиента")
  private String middleName;

  @NotNull
  @Schema(description = "Пол клиента", requiredMode = Schema.RequiredMode.REQUIRED)
  private Gender gender;

  @NotNull
  @Past(message = "Дата рождения указана некорректно.")
  @Schema(description = "Дата рождения клиента", requiredMode = Schema.RequiredMode.REQUIRED, example = "1990-09-20")
  private LocalDate birthdate;

  @NotNull
  @Pattern(regexp = "\\d{4}", message = "Серия паспорта должна содержать 4 цифры.")
  @Schema(description = "Серия паспорта клиента", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024")
  private String passportSeries;

  @NotNull
  @Pattern(regexp = "\\d{6}", message = "Номер паспорта должен содержать 6 цифр.")
  @Schema(description = "Номер паспорта клиента", requiredMode = Schema.RequiredMode.REQUIRED, example = "000001")
  private String passportNumber;

  @NotNull
  @PastOrPresent
  @Schema(description = "Дата выдачи паспорта клиента", requiredMode = Schema.RequiredMode.REQUIRED, example = "2000-09-20")
  private LocalDate passportIssueDate;

  @NotNull
  @Pattern(regexp = "\\d{3}-\\d{3}",
      message = "Код подразделения состоит из 6 цифр в формате XXX-XXX.")
  @Schema(description = "Код подразделения, где был выдан паспорт клиента", requiredMode = Schema.RequiredMode.REQUIRED,
      example = "560-400")
  private String passportIssueBranch;

  @NotNull
  @Schema(description = "Семейное положение клиента", requiredMode = Schema.RequiredMode.REQUIRED)
  private MaritalStatus maritalStatus;

  @NotNull
  @Schema(description = "Количество иждивенцев на попечении клиента", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
  private Integer dependentAmount;

  @NotNull
  @Valid
  @Schema(description = "Данные о работе клиента", requiredMode = Schema.RequiredMode.REQUIRED)
  private EmploymentDto employmentDto;

  @NotNull
  @Size(min = 20, max = 20, message = "Номер счёта состоит из 20 цифр.")
  @Schema(description = "Номер счёта клиента", requiredMode = Schema.RequiredMode.REQUIRED, example = "40802810064580000000")
  private String accountNumber;

  @NotNull
  @Schema(description = "Наличие страховки по кредиту", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
  private Boolean isInsuranceEnabled;

  @NotNull
  @Schema(description = "Наличие статуса зарплатного клиента", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
  private Boolean isSalaryClient;
}
