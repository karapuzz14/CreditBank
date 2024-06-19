package ru.neostudy.creditbank.deal.model.attribute;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neostudy.creditbank.deal.enums.ApplicationStatus;
import ru.neostudy.creditbank.deal.enums.ChangeType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementStatusHistoryDto {

  @Enumerated(EnumType.STRING)
  private ApplicationStatus status;

  private LocalDateTime time;

  @Enumerated(EnumType.STRING)
  private ChangeType changeType;

}
