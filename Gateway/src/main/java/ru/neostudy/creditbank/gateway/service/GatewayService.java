package ru.neostudy.creditbank.gateway.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neostudy.creditbank.gateway.dto.FinishRegistrationRequestDto;
import ru.neostudy.creditbank.gateway.dto.LoanOfferDto;
import ru.neostudy.creditbank.gateway.dto.LoanStatementRequestDto;
import ru.neostudy.creditbank.gateway.dto.entity.Statement;
import ru.neostudy.creditbank.gateway.exception.DefaultException;
import ru.neostudy.creditbank.gateway.interfaces.DealClient;
import ru.neostudy.creditbank.gateway.interfaces.StatementClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class GatewayService {

  private final DealClient dealClient;
  private final StatementClient statementClient;

  public List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatementRequestDto)
      throws DefaultException {
    return statementClient.createLoanOffers(loanStatementRequestDto);
  }

  public void selectOffer(LoanOfferDto loanOfferDto) throws DefaultException {
    statementClient.selectOffer(loanOfferDto);
  }

  public void finishRegistration(FinishRegistrationRequestDto finishRegistrationRequestDto,
      String statementId) throws DefaultException {
    dealClient.finishRegistration(finishRegistrationRequestDto, statementId);
  }

  public void sendDocuments(String statementId) throws DefaultException {
    dealClient.sendDocuments(statementId);
  }

  public void signDocuments(Boolean isAccepted, String statementId) throws DefaultException {
    dealClient.signDocuments(isAccepted, statementId);
  }

  public void sendCodeVerification(String code, String statementId) throws DefaultException {
    dealClient.sendCodeVerification(code, statementId);
  }

  public Statement getStatementById(String statementId) throws DefaultException {
    return dealClient.getStatementById(statementId);
  }

  public List<Statement> getAllStatements() throws DefaultException {
    return dealClient.getAllStatements();
  }
}
