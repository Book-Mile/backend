package com.bookmile.backend.global.jwt.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.bookmile.backend.global.common.StatusCode.FORBIDDEN_TOKEN;

@Slf4j
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 유저 정보는 있으나, 자원 접근 권한이 없음 (403에러)
        log.warn("AccessDeniedHandlerImpl.handle() : 해당 회원에게 권한이 없음");
        response.setStatus(FORBIDDEN_TOKEN.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(FORBIDDEN_TOKEN.getMessage());
    }
}
