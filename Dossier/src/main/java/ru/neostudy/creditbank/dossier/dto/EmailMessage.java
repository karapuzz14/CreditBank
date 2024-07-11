package ru.neostudy.creditbank.dossier.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neostudy.creditbank.dossier.enums.Theme;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EmailMessage {

  private String address;

  private Theme theme;

  private UUID statementId;
}
