package ru.neostudy.creditbank.gateway.interfaces;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.neostudy.creditbank.gateway.config.FeignConfig;
import ru.neostudy.creditbank.gateway.dto.FinishRegistrationRequestDto;
import ru.neostudy.creditbank.gateway.dto.entity.Statement;
import ru.neostudy.creditbank.gateway.exception.CustomErrorDecoder;
import ru.neostudy.creditbank.gateway.exception.DefaultException;

@FeignClient(value = "deal", configuration = {FeignConfig.class, CustomErrorDecoder.class})
public interface DealClient {


  @PostMapping("/calculate/{statementId}")
  void finishRegistration(FinishRegistrationRequestDto finishRequest,
      @PathVariable String statementId) throws DefaultException;

  @GetMapping("/document/{statementId}/send")
  void sendDocuments(@PathVariable String statementId) throws DefaultException;

  @GetMapping("/document/{statementId}/sign")
  void signDocuments(@RequestParam("decision") Boolean isAccepted,
      @PathVariable String statementId) throws DefaultException;

  @GetMapping("/document/{statementId}/code")
  void sendCodeVerification(@RequestParam("code") String code, @PathVariable String statementId)
      throws DefaultException;

  @GetMapping("/admin/statement/{statementId}")
  Statement getStatementById(@PathVariable String statementId) throws DefaultException;

  @GetMapping("/admin/statement")
  List<Statement> getAllStatements() throws DefaultException;

}
