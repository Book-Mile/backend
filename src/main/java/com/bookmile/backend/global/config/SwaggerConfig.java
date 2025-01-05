package com.bookmile.backend.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Value("${swagger.server-url}")
    private String serverUrl;


    @Bean
    public OpenAPI openAPI() {
        Server server = new Server();
        server.setUrl(serverUrl);

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("Authorization");

        return new OpenAPI()
                .info(apiInfo())
                .addServersItem(server)
                .addSecurityItem(securityRequirement)
                .schemaRequirement("Authorization", securityScheme);
    }

    private Info apiInfo() {
        return new Info()
                .title("Book-Mile API")
                .description("도서 성취도 기록 서비스 API 문서입니다.")
                .version("1.0.0");
    }
}
