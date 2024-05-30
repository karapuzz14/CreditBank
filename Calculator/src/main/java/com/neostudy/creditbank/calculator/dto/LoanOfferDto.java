package com.neostudy.creditbank.calculator.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LoanOfferDto
{
    UUID statementId;
    @DecimalMax(value="30000000", message="Сумма кредита не может превышать 30 млн. рублей.")
    @DecimalMin(value="30000", message="Сумма кредита не может быть ниже 30 тыс. рублей.")
    BigDecimal requestedAmount;
    BigDecimal totalAmount;
    @Max(value=84, message="Максимальный срок кредита - 7 лет.")
    @Min(value=6, message="Минимальный срок кредита - 6 месяцев.")
    Integer term;
    BigDecimal monthlyPayment;
    BigDecimal rate;
    Boolean isInsuranceEnabled;
    Boolean isSalaryClient;

}
