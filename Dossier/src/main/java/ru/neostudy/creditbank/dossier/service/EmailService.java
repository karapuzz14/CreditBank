package ru.neostudy.creditbank.dossier.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import ru.neostudy.creditbank.dossier.dto.EmailMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

  @Value("${url}")
  private String dealUrl;

  private final JavaMailSender emailSender;

  public void sendFinishRegistrationEmail(EmailMessage message) {

    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setTo(message.getAddress());
    simpleMailMessage.setSubject("Завершение оформления кредита");
    simpleMailMessage.setText("Ваша заявка предварительно одобрена, завершите оформление.");
    emailSender.send(simpleMailMessage);
  }

  public void sendCreateDocumentsEmail(EmailMessage message) {

    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setTo(message.getAddress());
    simpleMailMessage.setSubject("Ваша заявка одобрена");

    String link = dealUrl + message.getStatementId() + "/send";

    simpleMailMessage.setText(
        "Ваша заявка на кредит одобрена. Перейти к оформлению документов можно по ссылке:\n"
            + link);
    emailSender.send(simpleMailMessage);
  }

  public void sendDocumentsEmail(EmailMessage message) {

    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setTo(message.getAddress());
    simpleMailMessage.setSubject("Подписание документов");
    simpleMailMessage.setText("<Здесь должно быть письмо с документами.");
    emailSender.send(simpleMailMessage);
  }

}