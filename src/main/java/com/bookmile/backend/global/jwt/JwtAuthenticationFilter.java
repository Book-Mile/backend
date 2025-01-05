package com.bookmile.backend.global.jwt;

import com.bookmile.backend.global.exception.CustomException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.bookmile.backend.global.common.StatusCode.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 Access Token 추출
        String accessToken = jwtTokenProvider.resolveToken(request);
        try{
            // 인증 토큰 생성 및 SecurityContext에 설정
            if( accessToken != null && jwtTokenProvider.validateToken(accessToken)){
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch(RedisConnectionFailureException e){
            SecurityContextHolder.clearContext();
            throw new CustomException(REDIS_ERROR);
        }catch(Exception e){
            throw new CustomException(INVALID_TOKEN);
        }

        // 다음 필터로 넘김
        filterChain.doFilter(request, response);
    }
}
