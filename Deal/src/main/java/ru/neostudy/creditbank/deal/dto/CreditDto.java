package ru.neostudy.creditbank.deal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Кредитное предложение")
public class CreditDto {

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
  @Schema(description = "Размер ежемесячной выплаты по кредиту", requiredMode = Schema.RequiredMode.REQUIRED, example = "9000.20")
  private BigDecimal monthlyPayment;

  @NotNull
  @Schema(description = "Процентная ставка по кредиту", requiredMode = Schema.RequiredMode.REQUIRED, example = "0.20")
  private BigDecimal rate;

  @NotNull
  @Schema(description = "Полная стоимость кредита", requiredMode = Schema.RequiredMode.REQUIRED, example = "100000.00")
  private BigDecimal psk;

  @NotNull
  @Schema(description = "Наличие страховки по кредиту", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
  private Boolean isInsuranceEnabled;

  @NotNull
  @Schema(description = "Наличие статуса зарплатного клиента", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
  private Boolean isSalaryClient;

  @NotNull
  @Valid
  @Schema(description = "Расписание ежемесячных выплат по кредиту до погашения долга", requiredMode = Schema.RequiredMode.REQUIRED)
  private List<PaymentScheduleElementDto> paymentSchedule;
}
