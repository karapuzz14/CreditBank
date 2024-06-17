package ru.neostudy.creditbank.deal.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.neostudy.creditbank.deal.dto.EmploymentDto;
import ru.neostudy.creditbank.deal.enums.Gender;
import ru.neostudy.creditbank.deal.enums.MaritalStatus;
import ru.neostudy.creditbank.deal.model.attribute.Passport;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID clientId;

  private String lastName;

  private String firstName;

  private String middleName;

  @Column(name = "birth_date")
  private LocalDate birthdate;

  private String email;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Enumerated(EnumType.STRING)
  private MaritalStatus maritalStatus;

  private Integer dependentAmount;

  @JdbcTypeCode(SqlTypes.JSON)
  private Passport passport;

  @JdbcTypeCode(SqlTypes.JSON)
  private EmploymentDto employment;

  private String accountNumber;
}
