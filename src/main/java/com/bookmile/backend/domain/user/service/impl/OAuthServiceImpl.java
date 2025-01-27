package com.bookmile.backend.domain.user.service.impl;

import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.user.entity.UserOAuth;
import com.bookmile.backend.domain.user.repository.UserOAuthRepository;
import com.bookmile.backend.domain.user.repository.UserRepository;
import com.bookmile.backend.domain.user.service.OAuthService;
import com.bookmile.backend.global.common.StatusCode;
import com.bookmile.backend.global.common.UserRole;
import com.bookmile.backend.global.exception.CustomException;
import com.bookmile.backend.global.jwt.JwtTokenProvider;
import com.bookmile.backend.global.oauth.OAuth2UnlinkService;
import com.bookmile.backend.global.common.RandomNickname;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bookmile.backend.global.common.StatusCode.AUTHENTICATION_FAILED;
import static com.bookmile.backend.global.common.StatusCode.INVALID_OAUTH_USER;

@Service
@RequiredArgsConstructor
@Log4j2
public class OAuthServiceImpl implements OAuthService {
    private final UserRepository userRepository;
    private final UserOAuthRepository userOAuthRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RandomNickname randomNickname;
    private final OAuth2UnlinkService oAuth2UnlinkService;

    @Value("${aws.main.profile}")
    private String mainProfile;

    @Value("${spring.oauth2.url.callback}")
    private String callBackUrl;

    // 회원의 소셜 연동 정보 조회
    @Override
    @Transactional
    public List<String> getOAuthProviders(String email) {
        User user = findByEmail(email);

        List<String> providers = userOAuthRepository.findByUserId(user.getId()).stream()
                .map(UserOAuth::getProvider)
                .toList();

        return providers;
    }


    @Override
    @Transactional
    public void unlinkUserOAuth(HttpServletRequest request, String provider, String email) {
        String token = jwtTokenProvider.resolveToken(request);

        User user = findByEmail(email);

        UserOAuth userOAuth = userOAuthRepository.findByUserIdAndProvider(user.getId(), provider).orElseThrow(
                () -> new CustomException(INVALID_OAUTH_USER));

        // 연동 해제
        if (provider.equals("kakao")){
            oAuth2UnlinkService.unlinkKakao(userOAuth.getProviderId());
        }else if(provider.equals("naver")){
            oAuth2UnlinkService.unlinkNaver(token);
        }else if(provider.equals("google")){
            oAuth2UnlinkService.unlinkGoogle(token);
        }else{
            throw new CustomException(StatusCode.INPUT_VALUE_INVALID);
        }

        userOAuthRepository.delete(userOAuth);
    }

    // [테스트용] - OAuth 로그인
    @Override
    public Map<String, String> testSocialLogin(String email) {

        // test용 유저 생성
        User testUser = userRepository.findByEmail(email).orElseGet(() -> {

            // 유저 정보 저장
            User newUser = userRepository.save( User.builder()
                    .email(email)
                    .nickname(randomNickname.generateNickname())
                    .image(mainProfile)
                    .role(UserRole.USER)
                    .isDeleted(false)
                    .build());

            // OAuth2.0 정보 저장
            userOAuthRepository.save( UserOAuth.builder()
                    .user(newUser)
                    .provider("test")
                    .providerId("test")
                    .build());
            return newUser;
        });

        // OAuth2User 생성
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", testUser.getEmail());
        attributes.put("exist", true);
        attributes.put("userId", testUser.getId());

        OAuth2User oAuth2User = new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes, "email");

        String accessToken = jwtTokenProvider.createTestAccessToken(testUser.getEmail(), testUser.getId(),
                testUser.getRole().toString());
        String refreshToken = jwtTokenProvider.createTestRefreshToken(testUser.getEmail(), testUser.getId());

        String redirectUrl = UriComponentsBuilder.fromHttpUrl(callBackUrl)
                .queryParam("testAccess", accessToken)
                .queryParam("testRefresh", refreshToken)
                .toUriString();

        log.info("UserServiceImpl.testSocialLogin: redirectUrl - {},", redirectUrl);

        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", redirectUrl);
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);

        return response;
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new CustomException(AUTHENTICATION_FAILED));
    }

}
