package ru.neostudy.creditbank.deal.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import ru.neostudy.creditbank.deal.dto.CreditDto;
import ru.neostudy.creditbank.deal.dto.FinishRegistrationRequestDto;
import ru.neostudy.creditbank.deal.dto.LoanOfferDto;
import ru.neostudy.creditbank.deal.dto.LoanStatementRequestDto;
import ru.neostudy.creditbank.deal.dto.ScoringDataDto;
import ru.neostudy.creditbank.deal.enums.ApplicationStatus;
import ru.neostudy.creditbank.deal.enums.ChangeType;
import ru.neostudy.creditbank.deal.enums.CreditStatus;
import ru.neostudy.creditbank.deal.interfaces.CalculatorClient;
import ru.neostudy.creditbank.deal.mapper.ClientMapperImpl;
import ru.neostudy.creditbank.deal.mapper.CreditMapperImpl;
import ru.neostudy.creditbank.deal.mapper.ScoringDataMapperImpl;
import ru.neostudy.creditbank.deal.model.attribute.Passport;
import ru.neostudy.creditbank.deal.model.attribute.StatementStatusHistoryDto;
import ru.neostudy.creditbank.deal.model.entity.Client;
import ru.neostudy.creditbank.deal.model.entity.Credit;
import ru.neostudy.creditbank.deal.model.entity.Statement;
import ru.neostudy.creditbank.deal.repository.ClientRepository;
import ru.neostudy.creditbank.deal.repository.CreditRepository;
import ru.neostudy.creditbank.deal.repository.StatementRepository;

@Service
public class DealService {

  private final ClientRepository clientRepository;
  private final StatementRepository statementRepository;
  private final CreditRepository creditRepository;
  private final CalculatorClient calculatorClient;
  private final ClientMapperImpl clientMapper;
  private final ScoringDataMapperImpl scoringDataMapper;
  private final CreditMapperImpl creditMapper;

  DealService(ClientRepository clientRepository, StatementRepository statementRepository,
      CreditRepository creditRepository, CalculatorClient calculatorClient,
      ClientMapperImpl clientMapper,
      ScoringDataMapperImpl scoringDataMapper, CreditMapperImpl creditMapper) {
    this.clientRepository = clientRepository;
    this.statementRepository = statementRepository;
    this.creditRepository = creditRepository;
    this.calculatorClient = calculatorClient;
    this.clientMapper = clientMapper;
    this.scoringDataMapper = scoringDataMapper;
    this.creditMapper = creditMapper;
  }

  public List<LoanOfferDto> createStatement(LoanStatementRequestDto statementRequest) {
    Client client = clientMapper.dtoToClient(statementRequest);
    Passport passport = Passport.builder()
        .series(statementRequest.getPassportSeries())
        .number(statementRequest.getPassportNumber())
        .build();
    client.setPassport(passport);
    Client savedClient = clientRepository.save(client);

    LocalDateTime now = LocalDateTime.now();
    StatementStatusHistoryDto statusHistory = StatementStatusHistoryDto.builder()
        .status(ApplicationStatus.PREAPPROVAL)
        .changeType(ChangeType.AUTOMATIC)
        .time(now)
        .build();
    Statement statement = Statement.builder()
        .client(savedClient)
        .status(ApplicationStatus.PREAPPROVAL)
        .creationDate(now)
        .statusHistory(List.of(statusHistory))
        .build();
    Statement savedStatement = statementRepository.save(statement);

    UUID statementId = savedStatement.getStatementId();
    //TODO: Обработка исключений!
    List<LoanOfferDto> offers = calculatorClient.getLoanOffers(statementRequest);
    for (LoanOfferDto offer : offers) {
      offer.setStatementId(statementId);
    }

    return offers;
  }

  public void selectOffer(LoanOfferDto appliedOffer) {
    Statement statement = statementRepository.getByStatementId(appliedOffer.getStatementId());

    statement.setStatus(ApplicationStatus.APPROVED);

    StatementStatusHistoryDto statusEntry = StatementStatusHistoryDto.builder()
        .status(ApplicationStatus.APPROVED)
        .time(LocalDateTime.now())
        .changeType(ChangeType.MANUAL)
        .build();
    statement.getStatusHistory().add(statusEntry);

    statement.setAppliedOffer(appliedOffer);
    statementRepository.save(statement);

  }

  public void createCredit(FinishRegistrationRequestDto finishRequest, String statementId) {

    Statement statement = statementRepository.getByStatementId(UUID.fromString(statementId));
    Client updatedClient = statement.getClient();
    clientMapper.updateAtFinish(finishRequest, updatedClient);
    clientRepository.save(updatedClient);

    ScoringDataDto scoringData = scoringDataMapper.clientDataToDto(updatedClient,
        updatedClient.getPassport(), statement.getAppliedOffer());
    //TODO: Обработка исключений!
    CreditDto creditDto = calculatorClient.getCredit(scoringData);
    Credit credit = creditMapper.dtoToCredit(creditDto);
    credit.setCreditStatus(CreditStatus.CALCULATED);
    Credit savedCredit = creditRepository.save(credit);

    StatementStatusHistoryDto statusEntry = StatementStatusHistoryDto.builder()
        .status(ApplicationStatus.CC_APPROVED)
        .changeType(ChangeType.AUTOMATIC)
        .time(LocalDateTime.now())
        .build();
    statement.setCredit(savedCredit);
    statement.setStatus(ApplicationStatus.CC_APPROVED);
    statement.getStatusHistory().add(statusEntry);
    statementRepository.save(statement);

  }
}
