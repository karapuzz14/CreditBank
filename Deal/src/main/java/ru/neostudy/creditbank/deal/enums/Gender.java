package ru.neostudy.creditbank.deal.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {

  MALE("мужской"),

  FEMALE("женский");

  private final String docName;

}
