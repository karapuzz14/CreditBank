package ru.neostudy.creditbank.gateway.dto.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neostudy.creditbank.gateway.dto.PaymentScheduleElementDto;
import ru.neostudy.creditbank.gateway.enums.CreditStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные о кредите")
public class Credit {

  @Schema(description = "Уникальный идентификатор кредита")
  private UUID creditId;

  @Schema(description = "Сумма кредита")
  private BigDecimal amount;

  @Schema(description = "Срок кредита (мес.)", example = "24")
  private Integer term;

  @Schema(description = "Ежемесячный платёж (руб.)")
  private BigDecimal monthlyPayment;

  @Schema(description = "Ставка по кредиту (%)")
  private BigDecimal rate;

  @Schema(description = "Полная стоимость кредита (руб.)")
  private BigDecimal psk;

  @Schema(description = "Расписание платежей")
  private List<PaymentScheduleElementDto> paymentSchedule;

  @Schema(description = "Страховка")
  private boolean insuranceEnabled;

  @Schema(description = "Зарплатный клиент")
  private boolean salaryClient;

  @Schema(description = "Статус кредита")
  private CreditStatus creditStatus;
}
