package ru.neostudy.creditbank.deal.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MaritalStatus {

  SINGLE("холост/не замужем"),

  MARRIED("женат/замужем"),

  DIVORCED("разведен(а)");

  private final String docName;

  }
