package com.bookmile.backend.global.oauth;

import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.global.common.UserRole;
import com.bookmile.backend.global.exception.CustomException;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static com.bookmile.backend.global.common.StatusCode.PROVIDER_NOT_FOUND;

@Getter
public class OAuth2UserInfo {
    private final String provider;
    private final String providerId;
    private final String name;
    private final String email;
    private final String profile;

    @Builder
    private OAuth2UserInfo(String provider, String providerId, String name, String email, String profile) {
        this.provider = provider;
        this.providerId = providerId;
        this.name = name;
        this.email = email;
        this.profile = profile;
    }

    public static OAuth2UserInfo of(String provider, String providerId, Map<String, Object> attributes){
        return switch (provider) {
            case "google" -> ofGoogle(providerId, attributes);
            case "kakao" -> ofKakao(providerId, attributes);
            case "naver" -> ofNaver(providerId, attributes);
            default -> throw new CustomException(PROVIDER_NOT_FOUND);
        };
    }

    private static OAuth2UserInfo ofGoogle(String providerId, Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .provider("google")
                .providerId(providerId)
                .name(String.valueOf(attributes.get("name")))
                .email(String.valueOf(attributes.get("email")))
                .profile((String) attributes.get("picture"))
                .build();
    }

    private static OAuth2UserInfo ofKakao(String providerId, Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        String name = emailToName(String.valueOf(account.get("email")), String.valueOf(account.get("name")));

        return OAuth2UserInfo.builder()
                .provider("kakao")
                .providerId(providerId)
                .name(name)
                .email(String.valueOf(account.get("email")))
                .profile(String.valueOf(profile.get("profile_image_url")))
                .build();
    }

    private static OAuth2UserInfo ofNaver(String providerId, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2UserInfo.builder()
                .provider("naver")
                .providerId(providerId)
                .name(String.valueOf(response.get("name")))
                .email(String.valueOf(response.get("email")))
                .profile(String.valueOf(response.get("profile_image")))
                .build();
    }
    // kakao의 경우, name이 들어오지 않으므로-> 이메일을 따옴.
    private static String emailToName(String email, String name) {
        if(name == null || name.isEmpty()) {
            name = email.split("@")[0];
        }
        return name;
    }

    // OAuth2.0을 통해 유저 정보 저장
    public User toEntity() {
        return User.builder()
                .nickname(name)
                .email(email)
                .image(profile)
                .provider(provider)
                .providerId(providerId)
                .role(UserRole.USER)
                .isDeleted(false)
                .build();
    }
}
