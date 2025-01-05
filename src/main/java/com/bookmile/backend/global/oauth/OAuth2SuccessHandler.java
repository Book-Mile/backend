package com.bookmile.backend.global.oauth;

import com.bookmile.backend.global.common.UserRole;
import com.bookmile.backend.global.jwt.CustomUserDetails;
import com.bookmile.backend.global.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.security.Principal;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${spring.oauth2.url.sign-in}")
    private String signInUrl;

    @Value("${spring.oauth2.url.sign-up}")
    private String signUpUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2SuccessHandler.onAuthenticationSuccess: 진입. Authentication: {}", authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String email = userDetails.getUsername();
        Long userId = userDetails.getUser().getId();
        String userRole = userDetails.getUser().getRole().toString();

        // 이 로그 나중에 삭제
        log.info("OAuth2SuccessHandler.onAuthenticationSuccess(): 회원 정보 추출 email - {}, userId - {}", email, userId);

        String accessToken = jwtTokenProvider.createAccessToken(email, userId, userRole);
        String refreshToken = jwtTokenProvider.createRefreshToken(email,userId);

        String redirectUrl;
        if(userDetails.getUser().getRole().equals(UserRole.GUEST)){
            log.info("OAuth2SuccessHandler.onAuthenticationSuccess(): {} 이므로 회원가입 Redirect", userDetails.getUser().getRole().toString());

            redirectUrl =  UriComponentsBuilder.fromHttpUrl(signUpUrl)
                    .queryParam("email", email)
                    .queryParam("accessToken", accessToken)
                    .queryParam("refreshToken", refreshToken)
                    .toUriString();
        }else{
            log.info("OAuth2SuccessHandler.onAuthenticationSuccess(): {} 이므로 로그인 ", userDetails.getUser().getRole().toString());
            redirectUrl= UriComponentsBuilder.fromHttpUrl(signInUrl)
                    .queryParam("accessToken", accessToken)
                    .queryParam("refreshToken", refreshToken)
                    .toUriString();
        }


        log.info("OAuth2SuccessHandler.onAuthenticationSuccess: 토큰 발급 {}", accessToken);
        log.info("OAuth2SuccessHandler.onAuthenticationSuccess: 로그인 성공 리다이렉트url - {}", redirectUrl);

        response.sendRedirect(redirectUrl);
    }

    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {

    }
}
