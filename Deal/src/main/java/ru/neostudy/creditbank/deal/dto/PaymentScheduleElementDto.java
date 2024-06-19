package ru.neostudy.creditbank.deal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Элемент расписания выплат")
public class PaymentScheduleElementDto {

  @NotNull
  @Schema(description = "Порядковый номер выплаты")
  private Integer number;

  @NotNull
  @Schema(description = "Дата выплаты")
  private LocalDate date;

  @NotNull
  @Schema(description = "Полная сумма выплаты")
  private BigDecimal totalPayment;

  @NotNull
  @Schema(description = "Сумма выплаты процентов")
  private BigDecimal interestPayment;

  @NotNull
  @Schema(description = "Сумма выплаты на погашение основного долга")
  private BigDecimal debtPayment;

  @NotNull
  @Schema(description = "Остаток долга")
  private BigDecimal remainingDebt;

}
