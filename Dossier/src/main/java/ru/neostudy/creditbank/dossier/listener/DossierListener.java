package ru.neostudy.creditbank.dossier.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.neostudy.creditbank.dossier.dto.EmailMessage;
import ru.neostudy.creditbank.dossier.service.EmailService;

@Component
@RequiredArgsConstructor
public class DossierListener {

  private final EmailService emailService;
  @KafkaListener(topics = "finish-registration", groupId = "group1", containerFactory = "kafkaListenerContainerFactory")
  public void finishRegistrationListener(EmailMessage message) {
    emailService.sendFinishRegistrationEmail(message);
  }

  @KafkaListener(topics = "create-documents", groupId = "group1", containerFactory = "kafkaListenerContainerFactory")
  public void createDocumentsListener(EmailMessage message) {
    emailService.sendCreateDocumentsEmail(message);
  }

  @KafkaListener(topics = "send-documents", groupId = "group1", containerFactory = "kafkaListenerContainerFactory")
  public void sendDocumentsListener(EmailMessage message) {
    emailService.sendDocumentsEmail(message);
  }
}
