package ru.neostudy.creditbank.gateway.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmploymentStatus {

  UNEMPLOYED("Безработный"),

  SELF_EMPLOYED("Самозанятый"),

  EMPLOYED("Работает"),

  EMPLOYER("Владелец бизнеса");

  private final String docName;


}
