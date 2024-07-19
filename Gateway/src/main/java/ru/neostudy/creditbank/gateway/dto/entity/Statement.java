package ru.neostudy.creditbank.gateway.dto.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neostudy.creditbank.gateway.dto.LoanOfferDto;
import ru.neostudy.creditbank.gateway.enums.ApplicationStatus;
import ru.neostudy.creditbank.gateway.dto.attribute.StatementStatusHistoryDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные о заявке")
public class Statement {

  @Schema(description = "Идентификатор заявки")
  private UUID statementId;

  @Schema(description = "Данные о клиенте")
  private Client client;

  @Schema(description = "Данные о кредите")
  private Credit credit;

  @Schema(description = "Статус заявки")
  private ApplicationStatus status;

  @Schema(description = "Дата создания")
  private LocalDateTime creationDate;

  @Schema(description = "Выбранное кредитное предложение")
  private LoanOfferDto appliedOffer;

  @Schema(description = "Дата подписания договора")
  private LocalDateTime signDate;

  @Schema(description = "Код подтверждения подписи")
  private String sesCode;

  @Schema(description = "История статусов заявки")
  private List<StatementStatusHistoryDto> statusHistory;

}
