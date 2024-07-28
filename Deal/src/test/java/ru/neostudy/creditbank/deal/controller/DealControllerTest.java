package ru.neostudy.creditbank.deal.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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
import ru.neostudy.creditbank.deal.dto.EmploymentDto;
import ru.neostudy.creditbank.deal.dto.FinishRegistrationRequestDto;
import ru.neostudy.creditbank.deal.dto.LoanOfferDto;
import ru.neostudy.creditbank.deal.dto.LoanStatementRequestDto;
import ru.neostudy.creditbank.deal.enums.EmploymentStatus;
import ru.neostudy.creditbank.deal.enums.Gender;
import ru.neostudy.creditbank.deal.enums.MaritalStatus;
import ru.neostudy.creditbank.deal.enums.Position;
import ru.neostudy.creditbank.deal.exception.DefaultException;
import ru.neostudy.creditbank.deal.exception.DeniedException;
import ru.neostudy.creditbank.deal.service.AdminService;
import ru.neostudy.creditbank.deal.service.DealService;
import ru.neostudy.creditbank.deal.service.EmailService;


@WebMvcTest
@ActiveProfiles("test")
public class DealControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private DealService dealService;

  @MockBean
  private EmailService emailService;

  @MockBean
  private AdminService adminService;

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

  private FinishRegistrationRequestDto getFinishRegistrationRequestDto() {
    EmploymentDto employment = EmploymentDto.builder()
        .employmentStatus(EmploymentStatus.EMPLOYER)
        .employerINN("1024555125")
        .salary(new BigDecimal("25000"))
        .position(Position.ORDINARY)
        .workExperienceTotal(20)
        .workExperienceCurrent(4)
        .build();
    return FinishRegistrationRequestDto.builder()
        .gender(Gender.MALE)
        .maritalStatus(MaritalStatus.MARRIED)
        .dependentAmount(0)
        .passportIssueDate(LocalDate.of(2000, 1, 1))
        .passportIssueBranch("560-400")
        .accountNumber("40802810064580000000")
        .employmentDto(employment)
        .build();
  }

  @Test
  public void createLoanOffers() throws Exception {
    LoanStatementRequestDto request = getLoanStatementRequest();
    List<LoanOfferDto> response = getOffers();

    Mockito.when(dealService.createStatement(request)).thenReturn(response);

    mockMvc.perform(
            MockMvcRequestBuilders.post("/deal/statement")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(response)));

    Mockito.when(dealService.createStatement(request))
        .thenThrow(new DefaultException(LocalDateTime.now(), "default", "", ""));

    mockMvc.perform(
            MockMvcRequestBuilders.post("/deal/statement")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.code").value("default"));
  }

  @Test
  public void createLoanOffersWithIllegalArgument() throws Exception {

    mockMvc.perform(
            MockMvcRequestBuilders.post("/deal/statement")
                .content(getLoanStatementRequestWithIllegalArgument())
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("firstName"));
  }

  @Test
  public void createLoanOffersWithOtherException() throws Exception {

    mockMvc.perform(
            MockMvcRequestBuilders.post("/deal/statement/incorrect_address")
                .content(objectMapper.writeValueAsString(getLoanStatementRequest()))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.code").value("default"));
  }

  @Test
  public void selectOffer() throws Exception {
    LoanOfferDto request = getOffer();

    mockMvc.perform(
            MockMvcRequestBuilders.post("/deal/offer/select")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());
  }

  @Test
  public void finishRegistration() throws Exception {
    FinishRegistrationRequestDto request = getFinishRegistrationRequestDto();
    String statementId = UUID.randomUUID().toString();
    mockMvc.perform(
            MockMvcRequestBuilders.post("/deal/calculate/" + statementId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());

    Mockito.doThrow(new DeniedException()).when(dealService).createCredit(request, statementId);

    mockMvc.perform(
            MockMvcRequestBuilders.post("/deal/calculate/" + statementId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("cc_denied"));
  }

  @Test
  public void finishRegistrationWithDefaultException() throws Exception {

    FinishRegistrationRequestDto request = getFinishRegistrationRequestDto();
    String statementId = UUID.randomUUID().toString();

    Mockito.doThrow(new DefaultException(LocalDateTime.now(), "default", "", ""))
        .when(dealService).createCredit(request, statementId);

    mockMvc.perform(
            MockMvcRequestBuilders.post("/deal/calculate/" + statementId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.code").value("default"));
  }
}
