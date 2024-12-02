package com.bookmile.backend.global.utils;

import com.bookmile.backend.global.config.AladinApiConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.net.URLEncoder;

@Component
public class AladinClient {

    private final RestTemplate restTemplate;
    private final AladinApiConfig config;

    public AladinClient(RestTemplateBuilder restTemplateBuilder, AladinApiConfig config) {
        this.restTemplate = restTemplateBuilder.build();
        this.config = config;
    }

    public String searchBooks(String query, String queryType, int maxResults) throws Exception {
        // JSON 형식 반환
        String url = UriComponentsBuilder.fromHttpUrl(config.getBaseUrl() + "/ItemSearch.aspx")
                .queryParam("ttbkey", config.getTtbKey())
                .queryParam("Query", URLEncoder.encode(query, "UTF-8"))
                .queryParam("QueryType", queryType)
                .queryParam("MaxResults", maxResults)
                .queryParam("SearchTarget", "Book")
                .queryParam("output", "JS") // JSON 형식으로 요청
                .toUriString();

        // API 호출
        return restTemplate.getForObject(url, String.class);
    }
}