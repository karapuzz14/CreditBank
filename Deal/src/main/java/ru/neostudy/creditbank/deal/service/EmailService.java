package ru.neostudy.creditbank.deal.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.neostudy.creditbank.deal.dto.EmailMessage;
import ru.neostudy.creditbank.deal.dto.EmploymentDto;
import ru.neostudy.creditbank.deal.dto.PaymentScheduleElementDto;
import ru.neostudy.creditbank.deal.enums.ApplicationStatus;
import ru.neostudy.creditbank.deal.enums.ChangeType;
import ru.neostudy.creditbank.deal.enums.CreditStatus;
import ru.neostudy.creditbank.deal.enums.Theme;
import ru.neostudy.creditbank.deal.model.attribute.Passport;
import ru.neostudy.creditbank.deal.model.entity.Client;
import ru.neostudy.creditbank.deal.model.entity.Credit;
import ru.neostudy.creditbank.deal.model.entity.Statement;
import ru.neostudy.creditbank.deal.repository.CreditRepository;
import ru.neostudy.creditbank.deal.repository.StatementRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

  private final StatementRepository statementRepository;
  private final CreditRepository creditRepository;
  private final KafkaTemplate<String, EmailMessage> emailKafkaTemplate;
  private final KafkaTemplate<String, EmailMessage> fileKafkaTemplate;

  private final Random rand = new Random();
  private static final String datePattern = "dd.MM.yyyy";
  private static final String docCurrency = " руб.\n";
  private static final String termUnit = " мес.\n";

  public void sendDocuments(String topic, String statementId, ApplicationStatus status) {
    Statement statement = statementRepository.getByStatementId(UUID.fromString(statementId));

    statement.setStatusAndHistoryEntry(status, ChangeType.AUTOMATIC);
    statementRepository.save(statement);

    List<String> documents = getDocuments(statementId);
    EmailMessage message = EmailMessage.builder()
        .address(statement.getClient().getEmail())
        .theme(Theme.getThemeByTopic(topic))
        .statementId(statement.getStatementId())
        .documents(documents)
        .build();
    log.debug("Сформирован текст документов по заявке {}", statementId);

    fileKafkaTemplate.send(topic, message);
    logMessageSent(topic, message.toString());
  }

  public void sendCode(String topic, String statementId) {
    Statement statement = statementRepository.getByStatementId(UUID.fromString(statementId));

    int codeNumber = rand.nextInt(0, 9999);
    String code = String.format("%04d", codeNumber);

    statement.setSesCode(code);
    statementRepository.save(statement);
    log.debug("Сгенерирован код подтверждения {} для заявки {}.", code, statementId);

    EmailMessage message = EmailMessage.builder()
        .address(statement.getClient().getEmail())
        .theme(Theme.getThemeByTopic(topic))
        .statementId(statement.getStatementId())
        .code(code)
        .build();

    emailKafkaTemplate.send(topic, message);
    logMessageSent(topic, message.toString());
  }

  public void sendCreditIssuedMessage(String topic, String statementId, String code) {
    Statement statement = statementRepository.getByStatementId(UUID.fromString(statementId));

    if (statement.getSesCode().equals(code)) {
      statement.setStatusAndHistoryEntry(ApplicationStatus.DOCUMENT_SIGNED, ChangeType.MANUAL);
      statement.setStatusAndHistoryEntry(ApplicationStatus.CREDIT_ISSUED, ChangeType.AUTOMATIC);
      statement.setSignDate(LocalDateTime.now());

      statementRepository.save(statement);

      Credit credit = statement.getCredit();
      credit.setCreditStatus(CreditStatus.ISSUED);

      creditRepository.save(credit);

      log.debug("Полученный код подтверждения {} совпал с отправленным. Кредит по заявке {} выдан.",
          code, statementId);

      EmailMessage message = EmailMessage.builder()
          .address(statement.getClient().getEmail())
          .theme(Theme.getThemeByTopic(topic))
          .statementId(statement.getStatementId())
          .build();

      emailKafkaTemplate.send(topic, message);
      logMessageSent(topic, message.toString());
    }
  }

  public void changeStatementStatus(String statementId, ApplicationStatus status,
      ChangeType changeType) {
    Statement statement = statementRepository.getByStatementId(UUID.fromString(statementId));

    statement.setStatusAndHistoryEntry(status, changeType);

    statementRepository.save(statement);
  }

  private List<String> getDocuments(String statementId) {
    Statement statement = statementRepository.getByStatementId(UUID.fromString(statementId));

    String loanContract = createLoanContract(statement);
    String paymentSchedule = createPaymentSchedule(statement.getCredit());
    String application = createApplication(statement);

    return List.of(loanContract, application, paymentSchedule);
  }

  private String createLoanContract(Statement statement) {

    Client client = statement.getClient();
    Credit credit = statement.getCredit();

    String loanContract = "";
    loanContract += "Кредитный договор №" + statement.getStatementId() + "\n";
    loanContract += getClientInfo(client);
    loanContract += getCreditInfo(credit);

    return loanContract;
  }

  private String getClientInfo(Client client) {
    String clientInfo = "1. Информация о заёмщике.\n";

    clientInfo += "Номер счёта: " + client.getAccountNumber() + "\n";
    clientInfo += "ФИО: "
        + client.getLastName() + " "
        + client.getFirstName() + " "
        + client.getMiddleName() + "\n";
    clientInfo +=
        "Дата рождения: " + client.getBirthdate().format(DateTimeFormatter.ofPattern(datePattern))
            + "\n";
    clientInfo += "Адрес эл. почты: " + client.getEmail() + "\n";
    clientInfo += "Пол: " + client.getGender().getDocName() + "\n";
    clientInfo += "Семейное положение: " + client.getMaritalStatus().getDocName() + "\n";
    clientInfo += "Количество иждивенцев: " + client.getDependentAmount() + "\n";

    clientInfo += "Паспортные данные.\n";
    Passport passport = client.getPassport();
    clientInfo += "Номер: " + passport.getNumber() + "\n";
    clientInfo += "Серия: " + passport.getSeries() + "\n";
    clientInfo +=
        "Дата выдачи: " + passport.getIssueDate().format(DateTimeFormatter.ofPattern(datePattern))
            + "\n";
    clientInfo += "Код подразделения: " + passport.getIssueBranch() + "\n";

    clientInfo += "Информация о трудоустройстве.\n";
    EmploymentDto employment = client.getEmployment();
    clientInfo += "Заработная плата: " + employment.getSalary() + docCurrency;
    clientInfo += "Характер должности: " + employment.getPosition().getDocName() + "\n";
    clientInfo += "ИНН работодателя: " + employment.getEmployerINN() + "\n";
    clientInfo += "Статус трудоустройства: " + employment.getEmploymentStatus().getDocName() + "\n";
    clientInfo += "Общий опыт работы: " + employment.getWorkExperienceTotal() + termUnit;
    clientInfo += "Текущий опыт работы: " + employment.getWorkExperienceCurrent() + termUnit;

    return clientInfo;
  }

  private String getCreditInfo(Credit credit) {
    String creditInfo = "2. Информация о кредите.\n";

    creditInfo += "Сумма кредита: " + credit.getAmount() + docCurrency;
    creditInfo += "Срок: " + credit.getTerm() + termUnit;
    creditInfo += "Ежемесячный платёж: " + credit.getMonthlyPayment() + docCurrency;
    creditInfo += "Ставка: " + credit.getRate().multiply(new BigDecimal(100)) + "%\n";
    creditInfo += "Полная сумма кредита: " + credit.getPsk() + docCurrency;

    return creditInfo;
  }

  private String createPaymentSchedule(Credit credit) {
    String paymentSchedule = "График платежей.\n";
    List<PaymentScheduleElementDto> schedule = credit.getPaymentSchedule();

    for (PaymentScheduleElementDto element : schedule) {
      paymentSchedule += element.getNumber() + "-й платёж.\n";
      paymentSchedule +=
          "Дата платежа: " + element.getDate().format(DateTimeFormatter.ofPattern(datePattern))
              + "\n";
      paymentSchedule += "Общий размер платежа: " + element.getTotalPayment() + docCurrency;
      paymentSchedule += "Платеж по телу кредита: " + element.getDebtPayment() + docCurrency;
      paymentSchedule += "Платеж по процентам: " + element.getInterestPayment() + docCurrency;
      paymentSchedule += "Остаток долга: " + element.getRemainingDebt() + " руб.\n\n";
    }

    return paymentSchedule;
  }

  private String createApplication(Statement statement) {
    String application = "Анкета.\n";

    Client client = statement.getClient();
    Credit credit = statement.getCredit();

    application += "ФИО: "
        + client.getLastName() + " "
        + client.getFirstName() + " "
        + client.getMiddleName() + "\n";
    application += "Адрес эл. почты: " + client.getEmail() + "\n";
    application +=
        "Дата рождения: " + client.getBirthdate().format(DateTimeFormatter.ofPattern(datePattern))
            + "\n";

    Passport passport = client.getPassport();
    application += "Номер паспорта: " + passport.getNumber() + "\n";
    application += "Серия паспорта: " + passport.getSeries() + "\n";
    application += "Сумма кредита: " + credit.getAmount() + docCurrency;
    application += "Срок по кредиту: " + credit.getTerm() + termUnit;

    return application;
  }

  private void logMessageSent(String topic, String message) {
    log.debug("Отправлено сообщение в МС-Dossier через Kafka по теме {}: {}",
        topic, message);
  }
}
