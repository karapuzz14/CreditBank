package ru.neostudy.creditbank.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.neostudy.creditbank.deal.dto.CreditDto;
import ru.neostudy.creditbank.deal.model.entity.Credit;

@Mapper(componentModel = "spring")
public interface CreditMapper {

  @Mapping(source = "isInsuranceEnabled", target = "insuranceEnabled")
  @Mapping(source = "isSalaryClient", target = "salaryClient")
  Credit dtoToCredit(CreditDto creditDto);
}