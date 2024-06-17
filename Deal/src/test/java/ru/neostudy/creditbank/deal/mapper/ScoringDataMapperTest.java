package ru.neostudy.creditbank.deal.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import ru.neostudy.creditbank.deal.dto.EmploymentDto;
import ru.neostudy.creditbank.deal.dto.LoanOfferDto;
import ru.neostudy.creditbank.deal.dto.ScoringDataDto;
import ru.neostudy.creditbank.deal.enums.EmploymentStatus;
import ru.neostudy.creditbank.deal.enums.Gender;
import ru.neostudy.creditbank.deal.enums.MaritalStatus;
import ru.neostudy.creditbank.deal.enums.Position;
import ru.neostudy.creditbank.deal.model.attribute.Passport;
import ru.neostudy.creditbank.deal.model.entity.Client;

public class ScoringDataMapperTest {

  private final ScoringDataMapperImpl scoringDataMapper = new ScoringDataMapperImpl();

  public Client getClient() {
    EmploymentDto employment = EmploymentDto.builder()
        .employmentStatus(EmploymentStatus.EMPLOYER)
        .employerINN("1024555125")
        .salary(new BigDecimal("25000"))
        .position(Position.ORDINARY)
        .workExperienceTotal(20)
        .workExperienceCurrent(4)
        .build();
    Passport passport = Passport.builder()
        .series("0000")
        .number("000000")
        .issueDate(LocalDate.of(2000, 1, 1))
        .issueBranch("560-400")
        .build();

    return Client.builder()
        .lastName("Иванов")
        .firstName("Иван")
        .middleName("Иванович")
        .birthdate(LocalDate.of(2000, 10, 24))
        .email("ivanov@yandex.ru")
        .gender(Gender.MALE)
        .maritalStatus(MaritalStatus.MARRIED)
        .dependentAmount(0)
        .accountNumber("40802810064580000000")
        .employment(employment)
        .passport(passport)
        .build();
  }

  public LoanOfferDto getOffer() {
    return LoanOfferDto.builder()
        .requestedAmount(new BigDecimal("100000"))
        .totalAmount(new BigDecimal("113636.81"))
        .term(20)
        .monthlyPayment(new BigDecimal("5682.04"))
        .rate(new BigDecimal("0.15"))
        .isInsuranceEnabled(false)
        .isSalaryClient(false)
        .build();
  }
  @Test
  public void testScoringDataMapper() {

    EmploymentDto employment = EmploymentDto.builder()
        .employmentStatus(EmploymentStatus.EMPLOYER)
        .employerINN("1024555125")
        .salary(new BigDecimal("25000"))
        .position(Position.ORDINARY)
        .workExperienceTotal(20)
        .workExperienceCurrent(4)
        .build();

    LoanOfferDto offer = getOffer();

    ScoringDataDto expectedScoringDataDto = ScoringDataDto.builder()
        .lastName("Иванов")
        .firstName("Иван")
        .middleName("Иванович")
        .birthdate(LocalDate.of(2000, 10, 24))
        .gender(Gender.MALE)
        .maritalStatus(MaritalStatus.MARRIED)
        .dependentAmount(0)
        .accountNumber("40802810064580000000")
        .employmentDto(employment)
        .passportSeries("0000")
        .passportNumber("000000")
        .passportIssueDate(LocalDate.of(2000, 1, 1))
        .passportIssueBranch("560-400")
        .amount(offer.getRequestedAmount())
        .term(offer.getTerm())
        .isInsuranceEnabled(offer.getIsInsuranceEnabled())
        .isSalaryClient(offer.getIsSalaryClient())
        .build();

    Client client = getClient();
    ScoringDataDto actualScoringDataDto = scoringDataMapper.clientDataToDto(client, client.getPassport(), getOffer());

    assertEquals(expectedScoringDataDto, actualScoringDataDto);
  }
}
