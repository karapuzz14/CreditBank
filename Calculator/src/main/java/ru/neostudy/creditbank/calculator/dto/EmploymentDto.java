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

  @Schema(description = "Статус трудоустройства")
  private EmploymentStatus employmentStatus;
  @Pattern(regexp = "\\d{10,12}}", message = "Некорректный ИНН.")
  @Schema(description = "ИНН работодателя")
  private String employerINN;
  @NotNull
  @Schema(description = "Размер заработной платы")
  private BigDecimal salary;
  @NotNull
  @Schema(description = "Должность")
  private Position position;
  @PositiveOrZero
  @Schema(description = "Общий стаж работы")
  private Integer workExperienceTotal;
  @PositiveOrZero
  @Schema(description = "Текущий стаж работы")
  private Integer workExperienceCurrent;
}
