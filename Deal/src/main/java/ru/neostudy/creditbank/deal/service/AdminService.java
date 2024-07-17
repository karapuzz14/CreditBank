package ru.neostudy.creditbank.deal.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neostudy.creditbank.deal.model.entity.Statement;
import ru.neostudy.creditbank.deal.repository.StatementRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

  private final StatementRepository statementRepository;

  public Statement getStatementById(String statementId) {
    return statementRepository.getByStatementId(UUID.fromString(statementId));
  }

  public List<Statement> getAllStatements() {
    return statementRepository.findAll();
  }
}
