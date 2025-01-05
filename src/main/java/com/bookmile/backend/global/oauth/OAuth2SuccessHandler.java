package com.bookmile.backend.global.oauth;

import com.bookmile.backend.global.common.UserRole;
import com.bookmile.backend.global.exception.CustomException;
import com.bookmile.backend.global.jwt.CustomUserDetails;
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

            //CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

//            String email = userDetails.getUsername();
//            Long userId = userDetails.getUser().getId();
//            String userRole = userDetails.getUser().getRole().toString();
            String email = oAuth2User.getAttribute("email");
            String provider = oAuth2User.getAttribute("provider");
            String role = oAuth2User.getAuthorities().stream()
                    .findFirst()
                    .orElseThrow(IllegalAccessError::new)
                    .getAuthority();

            boolean isExist = oAuth2User.getAttribute("exist");

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
                String redirectUrl = UriComponentsBuilder.fromHttpUrl(signUpUrl)
                        .toUriString();
                response.sendRedirect(redirectUrl);
            }

//        }catch(CustomException e) {
//            log.error("OAuth2SuccessHandler.onAuthenticationSuccess: 중복 회원 로그인(회원가입) 시도 - {}", e.getMessage());
//            String redirectUrl = UriComponentsBuilder.fromHttpUrl(signUpUrl)
//                    .queryParam("error", e.getMessage())
//                    .toUriString();
//            response.sendRedirect(redirectUrl);
//        }
    }
}
