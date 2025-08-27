package com.interbanking.autentication.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Autenticación - Interbanking")
                .description("API para gestión de autenticación y JWT")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Interbanking Team")
                    .email("support@interbanking.com")));
    }
}
