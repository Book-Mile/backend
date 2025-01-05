package com.bookmile.backend.global.jwt;

import com.bookmile.backend.global.common.StatusCode;
import com.bookmile.backend.global.exception.CustomException;
import com.bookmile.backend.global.redis.RefreshToken;
import com.bookmile.backend.global.redis.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.bookmile.backend.global.common.StatusCode.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final CustomUserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${spring.jwt.secret}")
    private String secretKeyString;

    private SecretKey secretKey;

    @Value("${spring.jwt.access-token-valid-time}")
    private Long accessTokenValidTime;

    @Value("${spring.jwt.refresh-token-valid-time}")
    private Long refreshTokenValidTime;

    // SecretKey 초기화
    @PostConstruct
    protected void init() {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
    }
    // AccessToken 생성
    public String createAccessToken(String email, Long userId, String role) {
        Claims claims = (Claims) Jwts.claims().setSubject(email);
        claims.put("type", "accessToken");
        claims.put("userId", userId);
        claims.put("role", role);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .signWith(secretKey)
                .compact();
    }

    // RefreshToken 생성
    public String createRefreshToken(String email, Long userId) {
        Claims claims = (Claims) Jwts.claims().setSubject(email);
        claims.put("type", "refreshToken");
        claims.put("userId", userId);
        Date now = new Date();


        String refreshToken =  Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                    .signWith(secretKey)
                    .compact();

        // Redis 저장
        RefreshToken token = RefreshToken.builder()
                .userId(userId)
                .refreshToken(refreshToken)
                .build();
        refreshTokenRepository.save(token);

        return refreshToken;
    }

    //인증 성공후 SecurityContextHolder 에 담을 객체(Authentication) 생성
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserEmail(token));
        log.info("JwtTokenProvider.getAuthentication:  userDetails 가져옴 - {}", userDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserEmail(String token) {
        String email = parseClaims(token).getSubject();
        log.info("JwtTokenProvider.getUserEmail: 토큰 기반 회원 구별 정보(email) 추출 완료 - {}", email);
        return email;
    }

    public Long getUserId(String token) {
        Long userId = Long.valueOf(parseClaims(token).get("userId").toString());
        log.info("JwtTokenProvider.getUserId: 토큰 기반 회원 구별 정보 추출 완료, userId : {}", userId);
        return userId;
    }

    // http 헤더로부터 bearer 토큰 분리
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            log.info("JwtTokenProvider.resolveToken: 토큰 분리 {}",token);
            return token;
        }
        return null;
    }

    //토큰 검증
    public boolean validateToken(String token) {
        try{
            Claims claims = parseClaims(token);
            Date expiration = claims.getExpiration();
            if(expiration.before(new Date())) {
                String category = claims.get("type", String.class);
                if("refreshToken".equals(category)) {
                    throw new CustomException(REFRESH_TOKEN_EXPIRED);
                }
                throw new CustomException(ACCESS_TOKEN_EXPIRED);
            }
            return true;
        }catch (JwtException ex){
            throw ex;
        }
        catch (Exception e){
            log.error("JwtTokenProvider.validateToken: 토큰 유효 체크 예외 발생");
            return false;
        }
    }

    private Claims parseClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            log.info("JwtTokenProvider.parseClaims: 토큰 검증 완료, 파싱 클레임 - {}", claims);
            return claims;
        }catch(ExpiredJwtException e) {
            log.error("JwtTokenProvider.parseClaims: Jwt 만료됨");
        }catch(JwtException ex){
            log.error("JwtTokenProvider.parseClaims: Jwt 토큰 예외 발생 {}", ex.getMessage());
            throw ex;
        }
        return null;
    }

}
