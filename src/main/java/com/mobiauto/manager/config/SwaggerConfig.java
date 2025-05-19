package com.mobiauto.manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI mobiautoOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Mobiauto API")
                        .description("API para gestão de revendas de veículos")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Equipe Mobiauto")
                                .email("contato@mobiauto.com.br")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentação Mobiauto")
                        .url("https://mobiauto.com.br/docs"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                    .addSecuritySchemes("bearerAuth", new SecurityScheme()
                        .name("Authorization")
                        .type(Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(In.HEADER)
                    )
                );
    }
}