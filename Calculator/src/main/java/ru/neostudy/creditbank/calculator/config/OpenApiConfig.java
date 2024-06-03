package ru.neostudy.creditbank.calculator.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * Класс конфигурации Swagger.
 */
@OpenAPIDefinition(
    info = @Info(
        title = "Calculator API",
        description = "API для расчёта возможных и полных условий кредита.", version = "1.0.0",
        contact = @Contact(
            name = "Кирилл Лазарев",
            email = "kirilaz0201@gmail.com"
        )
    )
)
public class OpenApiConfig {

}

