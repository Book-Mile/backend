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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UnlinkService {

    private String GOOGLE_URL = "https://oauth2.googleapis.com/revoke";
    private String KAKAO_URL = "https://kapi.kakao.com/v1/user/unlink";
    private String NAVER_URL = "https://nid.naver.com/oauth2.0/token";


    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String NAVER_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String NAVER_CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.provider.kakao.admin-key}")
    private String KAKAO_ADMIN_KEY;

    private final RestTemplate restTemplate;

    public void unlinkKakao(String providerId) {

            // 헤더에 admin key 넣기
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + KAKAO_ADMIN_KEY);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("target_id_type", "user_id");
            params.add("target_id", providerId);
            log.info("unlinkKaKao : {}", params);

            HttpEntity<MultiValueMap<String,String>> requestEntity = new HttpEntity<>(params, headers);

            try {
                // 카카오 API 로 POST 호출
                ResponseEntity<Map> response = restTemplate.exchange(KAKAO_URL, HttpMethod.POST, requestEntity, Map.class);

                if (response.getStatusCode() == HttpStatus.OK) {
                    log.info("카카오 연동 해제 성공");
                } else {
                    log.error(" 카카오 연동 해제 실패: {}", response.getBody());
                }
            } catch(HttpClientErrorException e) {
                if(e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    log.error("KaKao 토큰 만료");
                    throw new CustomException(StatusCode.INVALID_TOKEN);
                }
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

    public void unlinkGoogle(String accessToken) {
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("token", accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        try {
            // 구글 API 로 POST 호출
            ResponseEntity<Map> response = restTemplate.exchange(GOOGLE_URL, HttpMethod.POST,requestEntity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("구글 연동 해제 성공");
            } else {
                log.error("구글 연동 해제 실패: {}", response.getBody());
            }
        } catch(HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                log.error("토큰 만료");
                throw new CustomException(StatusCode.INVALID_TOKEN);
            }
        }
    }

    // 네이버 응답 데이터
    @Getter
    @RequiredArgsConstructor
    public static class UnlinkResponse{
        @JsonProperty("access_token")
        private String accessToken;
        private String result;
    }
}
