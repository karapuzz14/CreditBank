package ru.neostudy.creditbank.gateway.dto.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neostudy.creditbank.gateway.dto.EmploymentDto;
import ru.neostudy.creditbank.gateway.enums.Gender;
import ru.neostudy.creditbank.gateway.enums.MaritalStatus;
import ru.neostudy.creditbank.gateway.dto.attribute.Passport;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Данные о клиенте")
public class Client {

  @Schema(description = "Уникальный идентификатор клиента")
  private UUID clientId;

  @Schema(description = "Фамилия клиента", example = "Краснов")
  private String lastName;

  @Schema(description = "Имя клиента", example = "Владимир")
  private String firstName;

  @Schema(description = "Отчество клиента")
  private String middleName;

  @Schema(description = "Дата рождения клиента", example = "1990-09-20")
  private LocalDate birthdate;

  @Schema(description = "Адрес электронной почты клиента", example = "testmail@yandex.ru")
  private String email;

  @Schema(description = "Пол клиента")
  private Gender gender;

  @Schema(description = "Семейное положение клиента")
  private MaritalStatus maritalStatus;

  @Schema(description = "Количество иждивенцев на попечении клиента", example = "1")
  private Integer dependentAmount;

  @Schema(description = "Паспортные данные клиента")
  private Passport passport;

  @Schema(description = "Данные о работе клиента")
  private EmploymentDto employment;

  @Schema(description = "Номер счёта клиента", example = "40802810064580000000")
  private String accountNumber;
}
