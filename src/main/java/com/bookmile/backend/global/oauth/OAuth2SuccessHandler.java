package com.bookmile.backend.global.oauth;

import com.bookmile.backend.global.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${spring.oauth2.url.callback}")
    private String callbackUrl;

    @Value("${spring.oauth2.url.sign-up}")
    private String signUpUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            log.info("OAuth2SuccessHandler.onAuthenticationSuccess: 진입. Authentication: {}", authentication);

            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

            String email = oAuth2User.getAttribute("email");
            String role = oAuth2User.getAuthorities().stream()
                    .findFirst()
                    .orElseThrow(IllegalAccessError::new)
                    .getAuthority();

            boolean isExist = oAuth2User.getAttribute("exist");

            // 회원 정보가 존재할 시
            if(isExist) {
                Long userId = (Long) oAuth2User.getAttributes().get("userId");
                String accessToken = jwtTokenProvider.createAccessToken(email, userId, role);
                String refreshToken = jwtTokenProvider.createRefreshToken(email, userId);
                log.info("OAuth2SuccessHandler: isExist 유저 - userId {}", userId);

                String redirectUrl = UriComponentsBuilder.fromHttpUrl(callbackUrl)
                        .queryParam("accessToken", accessToken)
                        .queryParam("refreshToken", refreshToken)
                        .toUriString();

                log.info("OAuth2SuccessHandler: redirectUrl {}", redirectUrl);
                response.sendRedirect(redirectUrl);
            }
            else{
                // 회원 정보 새로 저장 -> 로그인 페이지로 이동
                String redirectUrl = UriComponentsBuilder.fromHttpUrl(signUpUrl)
                        .toUriString();
                response.sendRedirect(redirectUrl);
            }

    }
}
