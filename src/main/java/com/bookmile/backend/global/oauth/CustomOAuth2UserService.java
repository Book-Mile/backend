package com.bookmile.backend.global.oauth;

import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.user.entity.UserOAuth;
import com.bookmile.backend.domain.user.repository.UserOAuthRepository;
import com.bookmile.backend.domain.user.repository.UserRepository;
import com.bookmile.backend.global.oauth.nickname.RandomNickname;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserOAuthRepository userOAuthRepository;
    private final RandomNickname randomNickname;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser: 유저 정보 가져오기 시작");
        // 유저 정보 가져오기
        OAuth2User oauth2User = super.loadUser(userRequest);

        // 클라이언트 등록 ID(google, naver, kakao)와 사용자 이름 속성을 가져온다.
        String provider= userRequest.getClientRegistration().getRegistrationId();

        // OAuth2UserService를 사용하여 가져온 OAuth2User 정보로 OAuth2UserInfo 객체를 만든다.
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(provider, oauth2User.getAttributes());
        //log.info("CustomOAuth2UserService.loadUser: OAuth2UserInfo - {}", oAuth2UserInfo);

        Map<String ,Object> userAttributes = oAuth2UserInfo.convertToMap();
        log.info("CustomOAuth2UserService.loadUser: userAttributes {}", userAttributes);

        String email = (String) userAttributes.get("email");
        String providerId = (String) userAttributes.get("attributeKey");
        log.info("CustomOAuth2UserService.loadUser: providerId {}", providerId);

        // User 정보 반환
        Optional<User> findUser = userRepository.findByEmail(email);
        boolean isFirstLogin = findUser.isEmpty();

        User user = findUser.orElseGet(() -> {
            // 새로운 유저 추가
            User newUser = userRepository.save(oAuth2UserInfo.toEntity(randomNickname));

            // 소셜 정보 저장
            userOAuthRepository.save(UserOAuth.builder()
                    .user(newUser)
                    .provider(provider)
                    .providerId(providerId)
                    .build());

            return newUser;
        });

        Optional<UserOAuth> existingOAuth = userOAuthRepository.findByProviderAndProviderId(provider, providerId);

        // 기존 유저 + 새로운 provider 추가
        if (!isFirstLogin && existingOAuth.isEmpty()) {
            userOAuthRepository.save(
                    UserOAuth.builder()
                            .user(user)
                            .provider(provider)
                            .providerId(providerId)
                            .build());
        }

        userAttributes.put("exist", !isFirstLogin); // 최초 로그인 여부에 따라 T/F
        userAttributes.put("userId", user.getId());
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString())),
                userAttributes, "email");

    }
}
