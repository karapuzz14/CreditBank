package ru.neostudy.creditbank.calculator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

  @NotNull
  @DecimalMax(value = "30000000", message = "Сумма кредита не может превышать 30 млн. рублей.")
  @DecimalMin(value = "30000", message = "Сумма кредита не может быть ниже 30 тыс. рублей.")
  @Schema(description = "Сумма кредита", requiredMode = Schema.RequiredMode.REQUIRED, example = "90000.00")
  private BigDecimal requestedAmount;

  @NotNull
  @Schema(description = "Полная стоимость кредита", requiredMode = Schema.RequiredMode.REQUIRED, example = "100000.00")
  private BigDecimal totalAmount;

  @Max(value = 84, message = "Максимальный срок кредита - 7 лет.")
  @Min(value = 6, message = "Минимальный срок кредита - 6 месяцев.")
  @NotNull
  @Schema(description = "Срок кредита (мес.)", requiredMode = Schema.RequiredMode.REQUIRED, example = "24")
  private Integer term;

  @NotNull
  @Schema(description = "Размер ежемесячной выплаты по кредиту", requiredMode = Schema.RequiredMode.REQUIRED, example = "9000.20")
  private BigDecimal monthlyPayment;

  @NotNull
  @Schema(description = "Процентная ставка по кредиту", requiredMode = Schema.RequiredMode.REQUIRED, example = "0.20")
  private BigDecimal rate;

  @NotNull
  @Schema(description = "Наличие страховки по кредиту", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
  private Boolean isInsuranceEnabled;

  @NotNull
  @Schema(description = "Наличие статуса зарплатного клиента", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
  private Boolean isSalaryClient;

}
