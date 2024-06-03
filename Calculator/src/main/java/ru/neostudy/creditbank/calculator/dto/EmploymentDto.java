package ru.neostudy.creditbank.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neostudy.creditbank.calculator.enums.EmploymentStatus;
import ru.neostudy.creditbank.calculator.enums.Position;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Schema(description = "Данные о работе клиента")
public class EmploymentDto {

  @NotNull
  @Schema(description = "Статус трудоустройства", required = true)
  private EmploymentStatus employmentStatus;

  @NotNull
  @Pattern(regexp = "\\d{10,12}", message = "Некорректный ИНН.")
  @Schema(description = "ИНН работодателя", required = true, example = "1024555125")
  private String employerINN;

  @NotNull
  @Schema(description = "Размер заработной платы", required = true, example = "10000.00")
  private BigDecimal salary;

  @NotNull
  @Schema(description = "Должность", required = true)
  private Position position;

  @NotNull
  @PositiveOrZero
  @Schema(description = "Общий стаж работы", required = true, example = "48")
  private Integer workExperienceTotal;

  @NotNull
  @PositiveOrZero
  @Schema(description = "Текущий стаж работы", required = true, example = "36")
  private Integer workExperienceCurrent;
}
