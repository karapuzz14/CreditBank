package com.neostudy.creditbank.calculator.dto;

import com.neostudy.creditbank.calculator.enums.EmploymentStatus;
import com.neostudy.creditbank.calculator.enums.Position;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EmploymentDto
{
    EmploymentStatus employmentStatus;
    @Pattern(regexp = "\\d{10,12}}", message = "Некорректный ИНН.")
    String employerINN;
    @NotNull
    BigDecimal salary;
    @NotNull
    Position position;
    @PositiveOrZero
    Integer workExperienceTotal;
    @PositiveOrZero
    Integer workExperienceCurrent;
}
