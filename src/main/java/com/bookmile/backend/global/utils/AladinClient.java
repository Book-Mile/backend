package com.bookmile.backend.global.utils;

import com.bookmile.backend.global.config.AladinApiConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;

@Component
public class AladinClient {

    private final RestTemplate restTemplate;
    private final AladinApiConfig config;

    public AladinClient(RestTemplate restTemplate, AladinApiConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    public String searchBooks(String query, String queryType, int maxResults) throws Exception {

            String url = UriComponentsBuilder.fromHttpUrl(config.getBaseUrl() + "/ItemSearch.aspx")
                .queryParam("ttbkey", config.getTtbKey())
                .queryParam("Query", URLEncoder.encode(query, "UTF-8"))
                .queryParam("QueryType", queryType)
                .queryParam("MaxResults", maxResults)
                .queryParam("start", "1")
                .queryParam("SearchTarget", "Book")
                .queryParam("output", "JS") // JSON 형식으로 요청
                .queryParam("Version", "20131101")

                .toUriString();
        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        System.out.println("Request URL: " + url); // 디버깅용
        System.out.println("API Response: " + response.getBody()); // 응답 데이터 출력
        return response.getBody();
    }
}