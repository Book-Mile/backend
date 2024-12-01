package com.bookmile.backend.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "aladin.api")
public class AladinApiConfig {
    private String baseUrl;
    private String ttbKey;
}
