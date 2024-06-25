package ru.neostudy.creditbank.calculator.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.neostudy.creditbank.calculator.dto.CreditDto;
import ru.neostudy.creditbank.calculator.dto.LoanOfferDto;
import ru.neostudy.creditbank.calculator.dto.LoanStatementRequestDto;
import ru.neostudy.creditbank.calculator.dto.PaymentScheduleElementDto;
import ru.neostudy.creditbank.calculator.dto.ScoringDataDto;
import ru.neostudy.creditbank.calculator.exception.DeniedException;
import ru.neostudy.creditbank.calculator.service.CreditService;
import ru.neostudy.creditbank.calculator.service.OfferService;

@WebMvcTest
@ActiveProfiles("test")
public class CalculatorControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CreditService creditService;

  @MockBean
  private OfferService offerService;

  @Autowired
  private ObjectMapper objectMapper;

  private List<LoanOfferDto> getOffers() {
    LoanOfferDto example = LoanOfferDto.builder()
        .requestedAmount(new BigDecimal("100000"))
        .totalAmount(new BigDecimal("113636.81"))
        .term(20)
        .monthlyPayment(new BigDecimal("5682.04"))
        .rate(new BigDecimal("0.15"))
        .isInsuranceEnabled(false)
        .isSalaryClient(false)
        .build();
    return List.of(example, example, example, example);
  }

  private LoanStatementRequestDto getLoanStatementRequest() {
    return LoanStatementRequestDto.builder()
        .amount(new BigDecimal("100000"))
        .term(20)
        .firstName("Иван")
        .lastName("Иванов")
        .middleName("Иванович")
        .email("ivanov@yandex.ru")
        .birthdate(LocalDate.of(2000, 10, 24))
        .passportSeries("0000")
        .passportNumber("000000")
        .build();
  }

  private ScoringDataDto getScoringData() throws JsonProcessingException {
    return objectMapper.readValue("""
        {
          "amount": "100000",
          "term": 20,
          "firstName": "Кирилл",
          "lastName": "Лазарев",
          "middleName": "Романович",
          "gender": 0,
          "birthdate": "2003-10-24",
          "passportSeries": "0000",
          "passportNumber": "000000",
          "passportIssueDate": "2005-10-24",
          "passportIssueBranch": "000-000",
          "maritalStatus": 0,
          "dependentAmount": 0,
          "employmentDto":\s
          {
              "employmentStatus": 3,
          "employerINN": "1024555125",
          "salary": "500",
          "position": 0,
          "workExperienceTotal": 20,
          "workExperienceCurrent": 4
          },
          "accountNumber": "00000000000000000000",
          "isInsuranceEnabled": "false",
          "isSalaryClient": "false"
        }""", ScoringDataDto.class);
  }

  private CreditDto getCredit() {
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

    return CreditDto.builder()
        .amount(new BigDecimal("100000"))
        .term(20)
        .rate(new BigDecimal("0.15"))
        .monthlyPayment(new BigDecimal("5776.84"))
        .psk(new BigDecimal("115532.07"))
        .paymentSchedule(schedule)
        .isInsuranceEnabled(false)
        .isSalaryClient(false)
        .build();

  }

  @Test
  public void calculateLoanOffers() throws Exception {
    LoanStatementRequestDto request = getLoanStatementRequest();
    List<LoanOfferDto> response = getOffers();

    Mockito.when(offerService.getOfferList(request)).thenReturn(response);

    mockMvc.perform(
            MockMvcRequestBuilders.post("/calculator/offers")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(response)));

  }

  @Test
  public void calculateLoanOffersWithOtherException() throws Exception {

    mockMvc.perform(
            MockMvcRequestBuilders.post("/calculator/offers/incorrect_address")
                .content(objectMapper.writeValueAsString(getLoanStatementRequest()))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.code").value("default"));
  }
  @Test
  public void calculateCreditOffer() throws Exception {
    ScoringDataDto request = getScoringData();
    CreditDto response = getCredit();

    Mockito.when(creditService.calculateCredit(request)).thenReturn(response);

    mockMvc.perform(
            MockMvcRequestBuilders.post("/calculator/calc")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(response)));

    Mockito.when(creditService.calculateCredit(request)).thenThrow(new DeniedException());

    mockMvc.perform(
            MockMvcRequestBuilders.post("/calculator/calc")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("cc_denied"));

  }
}
