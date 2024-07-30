package ru.neostudy.creditbank.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neostudy.creditbank.gateway.enums.Gender;
import ru.neostudy.creditbank.gateway.enums.MaritalStatus;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Schema(description = "Заявка на завершение регистрации")
public class FinishRegistrationRequestDto {

  @NotNull
  @Schema(description = "Пол клиента", requiredMode = Schema.RequiredMode.REQUIRED)
  private Gender gender;

  @NotNull
  @Schema(description = "Семейное положение клиента", requiredMode = Schema.RequiredMode.REQUIRED)
  private MaritalStatus maritalStatus;

  @NotNull
  @Schema(description = "Количество иждивенцев на попечении клиента", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
  private Integer dependentAmount;

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
  @Valid
  @Schema(description = "Данные о работе клиента", requiredMode = Schema.RequiredMode.REQUIRED)
  private EmploymentDto employmentDto;

  @NotNull
  @Size(min = 20, max = 20, message = "Номер счёта состоит из 20 цифр.")
  @Schema(description = "Номер счёта клиента", requiredMode = Schema.RequiredMode.REQUIRED, example = "40802810064580000000")
  private String accountNumber;

}
