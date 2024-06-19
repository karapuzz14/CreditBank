package ru.neostudy.creditbank.deal.model.attribute;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passport {

  private String series;

  private String number;

  private String issueBranch;

  private LocalDate issueDate;
}
