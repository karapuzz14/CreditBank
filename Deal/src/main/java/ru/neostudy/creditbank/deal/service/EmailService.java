package ru.neostudy.creditbank.deal.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.neostudy.creditbank.deal.dto.EmailMessage;
import ru.neostudy.creditbank.deal.enums.Theme;
import ru.neostudy.creditbank.deal.model.entity.Statement;
import ru.neostudy.creditbank.deal.repository.StatementRepository;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final StatementRepository statementRepository;
  private final KafkaTemplate<String, EmailMessage> emailKafkaTemplate;

  public void sendEmailMessage(String topic, String statementId) {
    Statement statement = statementRepository.getByStatementId(UUID.fromString(statementId));

    EmailMessage message = EmailMessage.builder()
        .address(statement.getClient().getEmail())
        .theme(Theme.getThemeByTopic(topic))
        .statementId(statement.getStatementId())
        .build();
    emailKafkaTemplate.send(topic, message);
  }
}
