package ru.neostudy.creditbank.gateway.dto.attribute;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Паспортные данные клиента")
public class Passport {

  @Schema(description = "Серия паспорта клиента", example = "2024")
  private String series;

  @Schema(description = "Номер паспорта клиента", example = "000001")
  private String number;

  @Schema(description = "Код подразделения, где был выдан паспорт клиента", example = "560-400")
  private String issueBranch;

  @Schema(description = "Дата выдачи паспорта клиента", example = "2000-09-20")
  private LocalDate issueDate;
}
