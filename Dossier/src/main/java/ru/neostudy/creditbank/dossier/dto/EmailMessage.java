package ru.neostudy.creditbank.dossier.dto;

import java.util.List;
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

  private String code;

  private List<String> documents;

}
