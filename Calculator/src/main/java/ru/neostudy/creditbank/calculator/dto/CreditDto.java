package ru.neostudy.creditbank.calculator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
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
  @Schema(description = "Сумма кредита")
  private BigDecimal amount;
  @Schema(description = "Срок кредита (мес.)")
  private Integer term;
  @Schema(description = "Размер ежемесячной выплаты по кредиту")
  private BigDecimal monthlyPayment;
  @Schema(description = "Процентная ставка по кредиту")
  private BigDecimal rate;
  @Schema(description = "Полная стоимость кредита")
  private BigDecimal psk;
  @Schema(description = "Наличие страховки по кредиту")
  private Boolean isInsuranceEnabled;
  @Schema(description = "Наличие статуса зарплатного клиента")
  private Boolean isSalaryClient;
  @Schema(description = "Расписание ежемесячных выплат по кредиту до погашения долга")
  private List<PaymentScheduleElementDto> paymentSchedule;
}
