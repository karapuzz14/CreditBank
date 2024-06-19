package ru.neostudy.creditbank.deal.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.neostudy.creditbank.deal.dto.PaymentScheduleElementDto;
import ru.neostudy.creditbank.deal.enums.CreditStatus;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Credit {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID creditId;

  private BigDecimal amount;

  private Integer term;

  private BigDecimal monthlyPayment;

  private BigDecimal rate;

  private BigDecimal psk;

  @JdbcTypeCode(SqlTypes.JSON)
  private List<PaymentScheduleElementDto> paymentSchedule;

  private boolean insuranceEnabled;

  private boolean salaryClient;

  @Enumerated(EnumType.STRING)
  private CreditStatus creditStatus;
}
