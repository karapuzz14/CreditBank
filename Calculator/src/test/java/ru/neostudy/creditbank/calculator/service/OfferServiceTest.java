package ru.neostudy.creditbank.calculator.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.neostudy.creditbank.calculator.dto.LoanOfferDto;
import ru.neostudy.creditbank.calculator.dto.LoanStatementRequestDto;
import ru.neostudy.creditbank.calculator.exception.LaterBirthdateException;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

  private final OfferService offerService = new OfferService();

  @Test
  void isDateLate() {
    LocalDate now = LocalDate.now();
    assertThrows(LaterBirthdateException.class,
        () -> offerService.isDateLate(now));
    assertThrows(LaterBirthdateException.class,
        () -> offerService.isDateLate(now.plusDays(1)));
    assertDoesNotThrow(() -> offerService.isDateLate(now.minusYears(18)));
    assertDoesNotThrow(() -> offerService.isDateLate(now.minusYears(40)));
  }

  @Test
  void getOfferList() {
    BigDecimal rate = BigDecimal.valueOf(0.15);
    int term = 20;
    LoanStatementRequestDto request = LoanStatementRequestDto.builder()
        .term(term)
        .amount(new BigDecimal("100000"))
        .build();
    ReflectionTestUtils.setField(offerService, "rate", rate);
    List<LoanOfferDto> offers = offerService.getOfferList(request);
    assertEquals(4, offers.size());
    assertEquals(request.getAmount(), offers.get(0).getRequestedAmount());
    assertEquals(request.getTerm(), offers.get(0).getTerm());
    assertTrue(offers.get(0).getRate()
        .compareTo(offers.get(3).getRate()) > 0);
    assertTrue(offers.get(0).getTotalAmount()
        .compareTo(offers.get(0).getRequestedAmount()) > 0);
    for (LoanOfferDto offer : offers) {
      if (offer.getIsInsuranceEnabled()) {
        assertTrue(request.getAmount().compareTo(offer.getRequestedAmount()) < 0);
      }
      if (offer.getIsSalaryClient()) {
        assertTrue(rate.compareTo(offers.get(3).getRate()) > 0);
      }
      assertNull(offer.getStatementId());
    }
  }

  @Test
  void calculateMonthlyPayment() {
    BigDecimal rate = BigDecimal.valueOf(0.15);
    int term = 20;
    BigDecimal amount = new BigDecimal("100000");
    ReflectionTestUtils.setField(offerService, "rate", rate);
    BigDecimal monthlyPayment = offerService.calculateMonthlyPayment(rate, amount, term);
    assertTrue(monthlyPayment.multiply(BigDecimal.valueOf(term))
        .compareTo(amount) > 0);

  }

  @Test
  void calculatePsk() {
    BigDecimal rate = BigDecimal.valueOf(0.15);
    int term = 20;
    BigDecimal amount = new BigDecimal("100000");
    ReflectionTestUtils.setField(offerService, "rate", rate);
    BigDecimal monthlyPayment = offerService.calculateMonthlyPayment(rate, amount, term);
    BigDecimal psk = offerService.calculatePsk(amount, monthlyPayment, rate);
    assertTrue(psk.compareTo(amount) > 0);
  }
}
