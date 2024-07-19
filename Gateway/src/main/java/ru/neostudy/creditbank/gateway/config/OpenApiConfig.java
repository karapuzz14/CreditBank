package ru.neostudy.creditbank.gateway.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * Класс конфигурации Swagger.
 */
@OpenAPIDefinition(
    info = @Info(
        title = "Gateway API",
        description = "API для инкапсуляции внутренней логики сервисов приложения.", version = "1.0.0",
        contact = @Contact(
            name = "Кирилл Лазарев",
            email = "kirilaz0201@gmail.com"
        )
    )
)
public class OpenApiConfig {

}
