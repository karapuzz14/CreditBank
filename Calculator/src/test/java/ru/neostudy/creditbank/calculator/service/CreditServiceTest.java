package ru.neostudy.creditbank.calculator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.neostudy.creditbank.calculator.dto.CreditDto;
import ru.neostudy.creditbank.calculator.dto.EmploymentDto;
import ru.neostudy.creditbank.calculator.dto.ScoringDataDto;
import ru.neostudy.creditbank.calculator.enums.EmploymentStatus;
import ru.neostudy.creditbank.calculator.enums.Gender;
import ru.neostudy.creditbank.calculator.enums.MaritalStatus;
import ru.neostudy.creditbank.calculator.enums.Position;

@ExtendWith(MockitoExtension.class)
public class CreditServiceTest {

  private final CreditService creditService = new CreditService();

  @Test
  void calculateCredit() {
    BigDecimal rate = new BigDecimal("0.15");
    int term = 20;
    ReflectionTestUtils.setField(creditService, "rate", rate);
    ScoringDataDto scoringData = new ScoringDataDto();
    scoringData.setEmploymentDto(EmploymentDto.builder()
        .employmentStatus(EmploymentStatus.UNEMPLOYED)
        .build());
    scoringData.setBirthdate(LocalDate.of(2000, 1, 1));

    assertNull(creditService.calculateCredit(scoringData));

    scoringData.setEmploymentDto(EmploymentDto.builder()
        .employmentStatus(EmploymentStatus.EMPLOYER)
        .employerINN("1020120121")
        .salary(BigDecimal.valueOf(20000))
        .position(Position.TOP_MANAGER)
        .workExperienceCurrent(20)
        .workExperienceTotal(40)
        .build());
    scoringData.setTerm(term);
    scoringData.setAmount(BigDecimal.valueOf(100000));
    scoringData.setMaritalStatus(MaritalStatus.MARRIED);
    scoringData.setGender(Gender.MALE);
    CreditDto credit = creditService.calculateCredit(scoringData);

    assertEquals(term, credit.getTerm());
    assertEquals(credit.getPaymentSchedule().size(), term);
    assertEquals(credit.getPaymentSchedule().get(term - 1).getRemainingDebt(),
        BigDecimal.valueOf(0).setScale(2, RoundingMode.CEILING));
    assertTrue(
        scoringData.getAmount().compareTo(credit.getPaymentSchedule().get(0).getRemainingDebt())
            > 0);

  }
}
