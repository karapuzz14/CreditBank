package ru.neostudy.creditbank.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.neostudy.creditbank.deal.dto.FinishRegistrationRequestDto;
import ru.neostudy.creditbank.deal.dto.LoanStatementRequestDto;
import ru.neostudy.creditbank.deal.model.entity.Client;

@Mapper(componentModel = "spring")
public interface ClientMapper {

  Client dtoToClient(LoanStatementRequestDto loanStatementRequestDto);

  @Mapping(source = "finishRequest.passportIssueDate", target = "client.passport.issueDate")
  @Mapping(source = "finishRequest.passportIssueBranch", target = "client.passport.issueBranch")
  @Mapping(source = "finishRequest.employmentDto", target = "client.employment")
  void updateAtFinish(FinishRegistrationRequestDto finishRequest, @MappingTarget Client client);
}