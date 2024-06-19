package ru.neostudy.creditbank.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.neostudy.creditbank.deal.dto.LoanOfferDto;
import ru.neostudy.creditbank.deal.dto.ScoringDataDto;
import ru.neostudy.creditbank.deal.model.attribute.Passport;
import ru.neostudy.creditbank.deal.model.entity.Client;

@Mapper(componentModel = "spring")
public interface ScoringDataMapper {

  @Mapping(source = "offer.requestedAmount", target = "amount")
  @Mapping(source = "passport.series", target = "passportSeries")
  @Mapping(source = "passport.number", target = "passportNumber")
  @Mapping(source = "passport.issueBranch", target = "passportIssueBranch")
  @Mapping(source = "passport.issueDate", target = "passportIssueDate")
  @Mapping(source = "client.employment", target = "employmentDto")
  ScoringDataDto clientDataToDto(Client client, Passport passport, LoanOfferDto offer);

}