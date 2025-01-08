package com.bookmile.backend.global.jwt.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.bookmile.backend.global.common.StatusCode.AUTHENTICATION_FAILED_TOKEN;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 유저 정보없이 접근한 경우
        log.warn("JwtAuthenticationEntryPoint.commence : 해당 회원에게 권한이 없음");
        response.setStatus(AUTHENTICATION_FAILED_TOKEN.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(AUTHENTICATION_FAILED_TOKEN.getMessage());
    }
}
