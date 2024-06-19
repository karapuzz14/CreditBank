package ru.neostudy.creditbank.deal.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.neostudy.creditbank.deal.dto.CreditDto;
import ru.neostudy.creditbank.deal.dto.PaymentScheduleElementDto;
import ru.neostudy.creditbank.deal.model.entity.Credit;

@ActiveProfiles("test")
public class CreditMapperTest {
  private final CreditMapperImpl creditMapper = new CreditMapperImpl();

  private List<PaymentScheduleElementDto> getSchedule() {
    PaymentScheduleElementDto scheduleElement = PaymentScheduleElementDto.builder()
        .number(1)
        .date(LocalDate.now())
        .debtPayment(new BigDecimal("5776.84"))
        .totalPayment(new BigDecimal("5776.84"))
        .interestPayment(new BigDecimal("5776.84"))
        .remainingDebt(new BigDecimal("95616.60"))
        .build();
    List<PaymentScheduleElementDto> schedule = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      schedule.add(scheduleElement);
    }
    return schedule;
  }

  private CreditDto getCreditDto() {
    return CreditDto.builder()
        .amount(new BigDecimal("100000"))
        .term(20)
        .monthlyPayment(new BigDecimal("5682.04"))
        .rate(new BigDecimal("0.15"))
        .psk(new BigDecimal("115532.07"))
        .paymentSchedule(getSchedule())
        .isInsuranceEnabled(false)
        .isSalaryClient(false)
        .build();
  }

  private Credit getExpectedCredit() {
    return Credit.builder()
        .amount(new BigDecimal("100000"))
        .term(20)
        .monthlyPayment(new BigDecimal("5682.04"))
        .rate(new BigDecimal("0.15"))
        .psk(new BigDecimal("115532.07"))
        .paymentSchedule(getSchedule())
        .insuranceEnabled(false)
        .salaryClient(false)
        .build();
  }
  @Test
  public void testCreditMapper() {

    Credit expectedCredit = getExpectedCredit();
    Credit actualCredit = creditMapper.dtoToCredit(getCreditDto());

    assertEquals(actualCredit, expectedCredit);

  }
}
