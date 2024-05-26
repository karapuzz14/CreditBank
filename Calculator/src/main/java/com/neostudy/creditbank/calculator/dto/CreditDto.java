package com.neostudy.creditbank.calculator.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CreditDto
{
    @DecimalMax(value="30000000", message="Сумма кредита не может превышать 30 млн. рублей.")
    @DecimalMin(value="30000", message="Сумма кредита не может быть ниже 30 тыс. рублей.")
    BigDecimal amount;
    @Max(value=84, message="Максимальный срок кредита - 7 лет.")
    @Min(value=6, message="Минимальный срок кредита - 6 месяцев.")
    Integer term;
    BigDecimal monthlyPayment;
    BigDecimal rate;
    BigDecimal psk;
    Boolean isInsuranceEnabled;
    Boolean isSalaryClient;
    List<PaymentScheduleElementDto> paymentSchedule;
}
