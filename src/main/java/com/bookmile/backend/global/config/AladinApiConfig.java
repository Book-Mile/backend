package com.bookmile.backend.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@ConfigurationProperties(prefix = "aladin.api")
@Getter
@Setter
public class AladinApiConfig {
    private String baseUrl;
    private String ttbKey;

    // RestTemplate Bean 설정
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .defaultHeader("Accept", "application/json") // JSON 응답을 요청
                .build();
    }   
}
