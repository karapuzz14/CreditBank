package ru.neostudy.creditbank.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Элемент расписания выплат")
public class PaymentScheduleElementDto {

  @Schema(description = "Порядковый номер выплаты")
  private Integer number;
  @Schema(description = "Дата выплаты")
  private LocalDate date;
  @Schema(description = "Полная сумма выплаты")
  private BigDecimal totalPayment;
  @Schema(description = "Сумма выплаты процентов")
  private BigDecimal interestPayment;
  @Schema(description = "Сумма выплаты на погашение основного долга")
  private BigDecimal debtPayment;
  @Schema(description = "Остаток долга")
  private BigDecimal remainingDebt;

}
