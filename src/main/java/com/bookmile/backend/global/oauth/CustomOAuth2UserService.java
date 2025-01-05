package com.bookmile.backend.global.oauth;

import com.bookmile.backend.domain.user.entity.User;
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
    private final RandomNickname randomNickname;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser: 유저 정보 가져오기 시작");
        // 유저 정보 가져오기
        OAuth2User oauth2User = super.loadUser(userRequest);

        // 클라이언트 등록 ID(google, naver, kakao)와 사용자 이름 속성을 가져온다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        log.info("CustomOAuth2UserService.loadUser: userNameAttributeName {} " , userNameAttributeName);

        // OAuth2UserService를 사용하여 가져온 OAuth2User 정보로 OAuth2UserInfo 객체를 만든다.
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, userNameAttributeName, oauth2User.getAttributes());
        log.info("CustomOAuth2UserService.loadUser: OAuth2UserInfo - {}", oAuth2UserInfo);

        Map<String ,Object> userAttributes = oAuth2UserInfo.convertToMap();

        String email = (String) userAttributes.get("email");

        // User 정보 반환
        Optional<User> findUser = userRepository.findByEmail(email);

        if(findUser.isEmpty()) {

            User newUser = userRepository.save(oAuth2UserInfo.toEntity(randomNickname));

            // 존재하지 않을 경우
            userAttributes.put("exist", false);
            userAttributes.put("userId", newUser.getId());
            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    userAttributes, "email");
        }

        // 존재하는 경우
        userAttributes.put("exist", true);
        userAttributes.put("userId", findUser.map(User::getId).orElse(null));
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(findUser.get().getRole().toString())),
                userAttributes, "email");

    }
}
