package ru.neostudy.creditbank.deal.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Position {

  ORDINARY("Рядовой сотрудник"),

  LOWER_MANAGER("Менеджер нижнего звена"),

  MIDDLE_MANAGER("Менеджер среднего звена"),

  TOP_MANAGER("Топ-менеджер");

  private final String docName;
}
