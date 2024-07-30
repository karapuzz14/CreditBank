package ru.neostudy.creditbank.dossier.service;

import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import ru.neostudy.creditbank.dossier.dto.EmailMessage;
import ru.neostudy.creditbank.dossier.exception.DefaultException;
import ru.neostudy.creditbank.dossier.interfaces.DealClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

  @Value("${url}")
  private String dealUrl;

  @Value("${documentPath}")
  private String documentPath;

  private final JavaMailSender emailSender;
  private final SpringTemplateEngine templateEngine;
  private final DealClient dealClient;

  private static final String emailEncoding = "UTF-8";

  public void sendFinishRegistrationEmail(EmailMessage message) {

    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setTo(message.getAddress());
    simpleMailMessage.setSubject("Завершение оформления кредита");
    simpleMailMessage.setText("Ваша заявка предварительно одобрена, завершите оформление.");

    emailSender.send(simpleMailMessage);
    logSimpleMailMessage(simpleMailMessage);

  }

  public void sendCreateDocumentsEmail(EmailMessage message) throws MessagingException {

    MimeMessage emailMessage = emailSender.createMimeMessage();

    MimeMessageHelper helper = new MimeMessageHelper(emailMessage, true, emailEncoding);
    helper.setTo(message.getAddress());
    helper.setSubject("Ваша заявка на кредит одобрена");

    Context templateContext = new Context();
    templateContext.setVariable("text", "Ваша заявка на кредит одобрена. "
        + "Для того, чтобы получить документы на почту, перейдите по ссылке:");
    templateContext.setVariable("link_text", "Сформировать документы");
    templateContext.setVariable("link", dealUrl + message.getStatementId());

    String htmlBody = templateEngine.process("api-link-template.html", templateContext);
    helper.setText(htmlBody, true);

    emailSender.send(emailMessage);
    logMimeMessage(emailMessage);

  }

  public void sendStatementDeniedEmail(EmailMessage message) {

    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setTo(message.getAddress());
    simpleMailMessage.setSubject("Ваша заявка отклонена");
    simpleMailMessage.setText("Ваша заявка на кредит отклонена.");

    emailSender.send(simpleMailMessage);
    logSimpleMailMessage(simpleMailMessage);

  }

  public void sendDocumentsEmail(EmailMessage message)
      throws MessagingException, IOException, DefaultException {

    MimeMessage emailMessage = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(emailMessage, true, emailEncoding);

    helper.setTo(message.getAddress());
    helper.setSubject("Документы по кредиту");

    Context templateContext = new Context();
    String statementId = message.getStatementId().toString();
    templateContext.setVariable("accept_link", dealUrl + statementId + "/sign?decision=true");
    templateContext.setVariable("reject_link", dealUrl + statementId + "/sign?decision=false");

    String htmlBody = templateEngine.process("api-choice-template.html", templateContext);
    helper.setText(htmlBody, true);

    List<File> filesToDelete = new ArrayList<>();

    for(int i = 0; i < message.getDocuments().size(); i++) {
      File file = new File(documentPath + "attach" + (i + 1) + "_" + statementId + ".txt");

      FileWriter writer = new FileWriter(file);
      writer.write(message.getDocuments().get(i));
      writer.close();
      log.debug("Создан временный файл документа {} по заявке {}", file.getName(), statementId);

      FileSystemResource attachment = new FileSystemResource(file);
      helper.addAttachment(file.getName(), attachment);

      filesToDelete.add(file);
    }

    emailSender.send(emailMessage);
    logMimeMessage(emailMessage);

    dealClient.changeStatementStatusOnDocumentsCreation(statementId);
    log.debug("Отправлен запрос на изменение статуса заявки {} на DOCUMENTS_CREATED", statementId);

    for(File file : filesToDelete) {
      Files.delete(file.toPath());
      log.debug("Удален временный файл документа {} по заявке {}", file.getName(), statementId);
    }
  }

  public void sendSignDocumentsEmail(EmailMessage message) throws MessagingException {
    MimeMessage emailMessage = emailSender.createMimeMessage();

    MimeMessageHelper helper = new MimeMessageHelper(emailMessage, true, emailEncoding);
    helper.setTo(message.getAddress());
    helper.setSubject("Подтверждение подписи");

    Context templateContext = new Context();
    templateContext.setVariable("text",
        "Ваше согласие на подпись документа было обработано. "
            + "Для того, чтобы подтвердить свою подпись, перейдите по ссылке:");
    templateContext.setVariable("link_text", "Подтвердить подпись");
    templateContext.setVariable("link", dealUrl + message.getStatementId() + "/sign/code?code=" + message.getCode());

    String htmlBody = templateEngine.process("api-link-template.html", templateContext);
    helper.setText(htmlBody, true);

    emailSender.send(emailMessage);
    logMimeMessage(emailMessage);
  }

  public void sendCreditIssuedEmail(EmailMessage message) {

    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setTo(message.getAddress());
    simpleMailMessage.setSubject("Кредит успешно выдан");
    simpleMailMessage.setText("Поздравляем с успешным завершением оформления кредита! Договор вступил в силу.");

    emailSender.send(simpleMailMessage);
    logSimpleMailMessage(simpleMailMessage);

  }

  private void logSimpleMailMessage(SimpleMailMessage message) {
    log.debug("Отправлено письмо по адресу {}. Тема: {}",
        message.getTo(),
        message.getSubject());
  }

  private void logMimeMessage(MimeMessage message) throws MessagingException {
    log.debug("Отправлено письмо по адресу {}. Тема: {}",
        message.getRecipients(RecipientType.TO)[0].toString(),
        message.getSubject());
  }
}