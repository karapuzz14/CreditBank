package ru.neostudy.creditbank.deal.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.neostudy.creditbank.deal.dto.LoanOfferDto;
import ru.neostudy.creditbank.deal.enums.ApplicationStatus;
import ru.neostudy.creditbank.deal.model.attribute.StatementStatusHistoryDto;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Statement {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID statementId;

  @OneToOne
  @JoinColumn(name = "client_id")
  private Client client;

  @OneToOne
  @JoinColumn(name = "credit_id")
  private Credit credit;

  @Enumerated(EnumType.STRING)
  private ApplicationStatus status;

  private LocalDateTime creationDate;

  @JdbcTypeCode(SqlTypes.JSON)
  private LoanOfferDto appliedOffer;

  private LocalDateTime signDate;

  private String sesCode;

  @JdbcTypeCode(SqlTypes.JSON)
  private List<StatementStatusHistoryDto> statusHistory;
}
