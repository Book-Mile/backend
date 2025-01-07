package com.bookmile.backend.global.oauth;

import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.global.common.UserRole;
import com.bookmile.backend.global.exception.CustomException;
import com.bookmile.backend.global.oauth.nickname.RandomNickname;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.bookmile.backend.global.common.StatusCode.PROVIDER_NOT_FOUND;

@Getter
public class OAuth2UserInfo {
    private final Map<String, Object> attributes; // 사용자 속성 정보를 담는 Map
    private final String attributeKey; // 사용자 속성의 키 값
    private final String provider;
    private final String name;
    private final String email;
    private final String profile;

    @Builder
    private OAuth2UserInfo(Map<String, Object> attributes, String attributeKey, String provider, String name, String email, String profile) {
        this.attributes = attributes;
        this.attributeKey = attributeKey;
        this.provider = provider;
        this.name = name;
        this.email = email;
        this.profile = profile;
    }

    public static OAuth2UserInfo of(String provider, String attributeKey, Map<String, Object> attributes){
        return switch (provider) {
            case "google" -> ofGoogle(attributeKey, attributes);
            case "kakao" -> ofKakao(attributeKey, attributes);
            case "naver" -> ofNaver(attributeKey, attributes);
            default -> throw new CustomException(PROVIDER_NOT_FOUND);
        };
    }

    private static OAuth2UserInfo ofGoogle(String attributeKey, Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .provider("google")
                .attributes(attributes)
                .attributeKey(attributeKey)
                .email(String.valueOf(attributes.get("email")))
                .profile((String) attributes.get("picture"))
                .build();
    }

    private static OAuth2UserInfo ofKakao(String attributeKey, Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
                .provider("kakao")
                .attributes(attributes)
                .attributeKey(attributeKey)
                .email(String.valueOf(account.get("email")))
                .profile(String.valueOf(profile.get("profile_image_url")))
                .build();
    }

    private static OAuth2UserInfo ofNaver(String attributeKey, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2UserInfo.builder()
                .provider("naver")
                .attributes(attributes)
                .attributeKey(attributeKey)
                .email(String.valueOf(response.get("email")))
                .profile(String.valueOf(response.get("profile_image")))
                .build();
    }

    // OAuth2.0을 통해 유저 정보 저장
    public User toEntity(RandomNickname nickname) {

        return User.builder()
                .nickname(nickname.generateNickname())
                .email(email)
                .image(profile)
                .provider(provider)
                .providerId(attributeKey)
                .role(UserRole.USER)
                .isDeleted(false)
                .build();
    }

    // OAuth2User 객체에 넣어주기 위해서 Map으로 값들을 반환해준다.
    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("provider", provider);
        map.put("attributeKey", attributeKey);

        return map;
    }
}
