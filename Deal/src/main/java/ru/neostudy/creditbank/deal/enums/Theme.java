package ru.neostudy.creditbank.deal.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum Theme {
  FINISH_REGISTRATION("finish-registration"),
  CREATE_DOCUMENTS("create-documents"),
  SEND_DOCUMENTS("send-documents"),
  SEND_SES("send-ses"),
  CREDIT_ISSUED("credit-issued"),
  STATEMENT_DENIED("statement-denied");

  private final String connectedTopic;

  public static Theme getThemeByTopic(String topic) {
    for (Theme theme : Theme.values()) {
      if (theme.getConnectedTopic().equals(topic)) {
        return theme;
      }
    }
    return null;
  }
}

