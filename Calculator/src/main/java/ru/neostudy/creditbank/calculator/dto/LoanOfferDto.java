package ru.neostudy.creditbank.calculator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Вариант кредитного предложения")
public class LoanOfferDto {

  @Schema(accessMode = Schema.AccessMode.READ_ONLY)
  private UUID statementId;
  @DecimalMax(value = "30000000", message = "Сумма кредита не может превышать 30 млн. рублей.")
  @DecimalMin(value = "30000", message = "Сумма кредита не может быть ниже 30 тыс. рублей.")
  @Schema(description = "Сумма кредита")
  private BigDecimal requestedAmount;
  @Schema(description = "Полная стоимость кредита")
  private BigDecimal totalAmount;
  @Max(value = 84, message = "Максимальный срок кредита - 7 лет.")
  @Min(value = 6, message = "Минимальный срок кредита - 6 месяцев.")
  @Schema(description = "Срок кредита (мес.)")
  private Integer term;
  @Schema(description = "Размер ежемесячной выплаты по кредиту")
  private BigDecimal monthlyPayment;
  @Schema(description = "Процентная ставка по кредиту")
  private BigDecimal rate;
  @Schema(description = "Наличие страховки по кредиту")
  private Boolean isInsuranceEnabled;
  @Schema(description = "Наличие статуса зарплатного клиента")
  private Boolean isSalaryClient;

}
