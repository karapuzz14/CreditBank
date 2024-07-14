package ru.neostudy.creditbank.dossier.interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.neostudy.creditbank.dossier.config.FeignConfig;
import ru.neostudy.creditbank.dossier.exception.CustomErrorDecoder;
import ru.neostudy.creditbank.dossier.exception.DefaultException;

@FeignClient(value = "deal", configuration = {FeignConfig.class, CustomErrorDecoder.class})
public interface DealClient {

  @RequestMapping(method = RequestMethod.PUT, value = "/document/{statementId}/status")
  void changeStatementStatusOnDocumentsCreation(@PathVariable("statementId") String statementId) throws DefaultException;

}
