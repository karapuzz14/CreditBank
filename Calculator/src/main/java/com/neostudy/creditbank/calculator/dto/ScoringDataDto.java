package com.neostudy.creditbank.calculator.dto;

import com.neostudy.creditbank.calculator.enums.Gender;
import com.neostudy.creditbank.calculator.enums.MaritalStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ScoringDataDto
{
    @DecimalMax(value="30000000", message="Сумма кредита не может превышать 30 млн. рублей.")
    @DecimalMin(value="30000", message="Сумма кредита не может быть ниже 30 тыс. рублей.")
    BigDecimal amount;
    @Max(value=84, message="Максимальный срок кредита - 7 лет.")
    @Min(value=6, message="Минимальный срок кредита - 6 месяцев.")
    Integer term;
    @Size(min=2, max=30, message="Длина имени: от 2 до 30 знаков.")
    String firstName;
    @Size(min=2, max=30, message="Длина фамилии: от 2 до 30 знаков.")
    String lastName;
    String middleName;
    Gender gender;
    @Past(message="Дата рождения указана некорректно.")
    LocalDate birthdate;
    @Pattern(regexp = "\\d{4}}", message = "Серия паспорта должна содержать 4 цифры.")
    String passportSeries;
    @Pattern(regexp = "\\d{6}}", message = "Номер паспорта должен содержать 6 цифр.")
    String passportNumber;
    LocalDate passportIssueDate;
    String passportIssueBranch;
    MaritalStatus maritalStatus;
    Integer dependentAmount;
    EmploymentDto employmentDto;
    String accountNumber;
    Boolean isInsuranceEnabled;
    Boolean isSalaryClient;
}
