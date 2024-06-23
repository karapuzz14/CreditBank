package ru.neostudy.creditbank.statement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.neostudy.creditbank.statement.interfaces.DealClient;

@SpringBootApplication
@EnableFeignClients
public class StatementApplication {

  public static void main(String[] args) {
    SpringApplication.run(StatementApplication.class, args);
  }

}
