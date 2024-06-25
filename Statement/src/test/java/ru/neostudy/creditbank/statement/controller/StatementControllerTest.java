package ru.neostudy.creditbank.statement.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import ru.neostudy.creditbank.statement.dto.LoanOfferDto;
import ru.neostudy.creditbank.statement.dto.LoanStatementRequestDto;
import ru.neostudy.creditbank.statement.exception.DefaultException;
import ru.neostudy.creditbank.statement.exception.LaterBirthdateException;
import ru.neostudy.creditbank.statement.service.StatementService;

@ActiveProfiles("test")
@WebMvcTest
public class StatementControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StatementService statementService;

  @Autowired
  private ObjectMapper objectMapper;

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

  private String getLoanStatementRequestWithIncorrectBirthdate() {
    return """
        {
         "amount": "30000",
         "term": 20,
         "firstName": "Иван",
         "lastName": "Иванов",
         "middleName": "Иванович",
         "email": "ivanov@yandex.ru",
         "birthdate": "24-10-2003",
         "passportSeries": "0000",
         "passportNumber": "000000"
        }""";
  }

  private String getLoanStatementRequestWithIllegalArgument() {
    return """
        {
         "amount": "30000",
         "term": 20,
         "firstName": "И",
         "lastName": "Иванов",
         "middleName": "Иванович",
         "email": "ivanov@yandex.ru",
         "birthdate": "2003-10-24",
         "passportSeries": "0000",
         "passportNumber": "000000"
        }""";
  }
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
  public void selectOffer() throws Exception {
    LoanOfferDto request = getOffer();

    mockMvc.perform(
            MockMvcRequestBuilders.post("/statement/offer")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());
  }

  @Test
  public void calculateLoanOffers()
      throws Exception {

    LoanStatementRequestDto request = getLoanStatementRequest();
    List<LoanOfferDto> response = getOffers();

    Mockito.when(statementService.getOffers(request)).thenReturn(response);

    mockMvc.perform(
            MockMvcRequestBuilders.post("/statement")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(response)));

    Mockito.when(statementService.getOffers(request)).thenThrow(new LaterBirthdateException());

    mockMvc.perform(
            MockMvcRequestBuilders.post("/statement")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("birthdate"));


  }

  @Test
  public void calculateLoanOffersWithIllegalArgument() throws Exception {

    mockMvc.perform(
            MockMvcRequestBuilders.post("/statement")
                .content(getLoanStatementRequestWithIllegalArgument())
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("firstName"));
  }

  @Test
  public void calculateLoanOffersWithIncorrectBirthdate() throws Exception {

    mockMvc.perform(
            MockMvcRequestBuilders.post("/statement")
                .content(getLoanStatementRequestWithIncorrectBirthdate())
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("birthdate"));
  }

  @Test
  public void calculateLoanOffersWithOtherException() throws Exception {

    mockMvc.perform(
            MockMvcRequestBuilders.post("/statement/incorrect_address")
                .content(getLoanStatementRequestWithIncorrectBirthdate())
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.code").value("default"));
  }

  @Test
  public void calculateLoanOffersWithDefaultException() throws Exception {

    LoanStatementRequestDto request = getLoanStatementRequest();
    Mockito.when(statementService.getOffers(request))
        .thenThrow(new DefaultException(LocalDateTime.now(), "default", "", ""));

    mockMvc.perform(
            MockMvcRequestBuilders.post("/statement")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.code").value("default"));
  }
}
