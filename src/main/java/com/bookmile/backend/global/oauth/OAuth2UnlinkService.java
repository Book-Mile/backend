package com.bookmile.backend.global.oauth;

import com.bookmile.backend.global.common.StatusCode;
import com.bookmile.backend.global.exception.CustomException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UnlinkService {

    private String GOOGLE_URL = "https://oauth2.googleapis.com/revoke";
    private String KAKAO_URL = "https://kapi.kakao.com/v1/user/unlink";
    private String NAVER_URL = "https://nid.naver.com/oauth2.0/token";

    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String NAVER_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String NAVER_CLIENT_SECRET;

    public void unlinkAccount(String provider, String accessToken) {
        switch (provider.toLowerCase()) {
            case "kakao":
                unlinkKakao(accessToken);
                break;
            case "naver":
                unlinkNaver(accessToken);
                break;
            case "google":
                unlinkGoogle(accessToken);
                break;
            default:
                throw new CustomException(StatusCode.INPUT_VALUE_INVALID);
        }
    }
    public void unlinkNaver(String accessToken) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "delete");
        params.add("client_id", NAVER_CLIENT_ID);
        params.add("client_secret", NAVER_CLIENT_SECRET);
        params.add("access_token", accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String,String>> requestEntity = new HttpEntity<>(params, headers);

        // 연동 해제 API 생성 => POST
        ResponseEntity<UnlinkResponse> response = restTemplate.exchange(NAVER_URL, HttpMethod.POST, requestEntity, UnlinkResponse.class);


        if(response.getBody() == null && !"success".equalsIgnoreCase(response.getBody().getResult())){
            throw new CustomException(StatusCode.SERVER_ERROR);
        }

        log.info("OAuth2UnlinkService.unlinkNaver: {}", response.getBody().getResult());
    }
    // 네이버 응답 데이터
    @Getter
    @RequiredArgsConstructor
    public static class UnlinkResponse{
        @JsonProperty("access_token")
        private String accessToken;
        private String result;
    }
