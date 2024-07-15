package ru.neostudy.creditbank.deal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:kafka-topics.properties")
@EnableFeignClients
public class DealApplication {

  public static void main(String[] args) {
    SpringApplication.run(DealApplication.class, args);
  }

}
