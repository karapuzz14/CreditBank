package ru.neostudy.creditbank.dossier.listener;

import jakarta.mail.MessagingException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.neostudy.creditbank.dossier.dto.EmailMessage;
import ru.neostudy.creditbank.dossier.exception.DefaultException;
import ru.neostudy.creditbank.dossier.service.EmailService;

@Component
@Slf4j
@RequiredArgsConstructor
public class DossierListener {

  private final EmailService emailService;
  @KafkaListener(topics = "finish-registration", groupId = "group1", containerFactory = "emailMessageListenerContainerFactory")
  public void finishRegistrationListener(EmailMessage message) {
    log.debug("Получено сообщение от МС-deal: {}", message.toString());

    emailService.sendFinishRegistrationEmail(message);
  }

  @KafkaListener(topics = "create-documents", groupId = "group1", containerFactory = "emailMessageListenerContainerFactory")
  public void createDocumentsListener(EmailMessage message) throws MessagingException {
    log.debug("Получено сообщение от МС-deal: {}", message.toString());

    emailService.sendCreateDocumentsEmail(message);
  }

  @KafkaListener(topics = "statement-denied", groupId = "group1", containerFactory = "emailMessageListenerContainerFactory")
  public void statementDeniedListener(EmailMessage message) {
    log.debug("Получено сообщение от МС-deal: {}", message.toString());

    emailService.sendStatementDeniedEmail(message);
  }

  @KafkaListener(topics = "send-documents", groupId = "group1", containerFactory = "emailMessageListenerContainerFactory")
  public void sendDocumentsListener(EmailMessage message)
      throws MessagingException, IOException, DefaultException {
    log.debug("Получено сообщение от МС-deal: {}", message.toString());

    emailService.sendDocumentsEmail(message);
  }

  @KafkaListener(topics = "send-ses", groupId = "group1", containerFactory = "emailMessageListenerContainerFactory")
  public void sendSes(EmailMessage message) throws MessagingException {
    log.debug("Получено сообщение от МС-deal: {}", message.toString());

    emailService.sendSignDocumentsEmail(message);
  }

  @KafkaListener(topics = "credit-issued", groupId = "group1", containerFactory = "emailMessageListenerContainerFactory")
  public void sendCreditIssued(EmailMessage message) {
    log.debug("Получено сообщение от МС-deal: {}", message.toString());

    emailService.sendCreditIssuedEmail(message);
  }
}
