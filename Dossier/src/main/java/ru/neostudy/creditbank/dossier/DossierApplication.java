package ru.neostudy.creditbank.dossier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableFeignClients
@PropertySource("classpath:dossier.properties")
public class DossierApplication {

  public static void main(String[] args) {
    SpringApplication.run(DossierApplication.class, args);
  }

}
