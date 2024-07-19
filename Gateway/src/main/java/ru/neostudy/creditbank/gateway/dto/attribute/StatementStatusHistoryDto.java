package ru.neostudy.creditbank.gateway.dto.attribute;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neostudy.creditbank.gateway.enums.ApplicationStatus;
import ru.neostudy.creditbank.gateway.enums.ChangeType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запись истории статусов заявки")
public class StatementStatusHistoryDto {

  @Schema(description = "Статус заявки")
  private ApplicationStatus status;

  @Schema(description = "Время присвоения статуса")
  private LocalDateTime time;

  @Schema(description = "Время присвоения статуса")

  private ChangeType changeType;

}
