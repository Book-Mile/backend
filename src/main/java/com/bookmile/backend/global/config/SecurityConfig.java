package com.bookmile.backend.global.config;

import com.bookmile.backend.global.jwt.handler.AccessDeniedHandlerImpl;
import com.bookmile.backend.global.jwt.handler.JwtAuthenticationEntryPoint;
import com.bookmile.backend.global.jwt.JwtAuthenticationFilter;
import com.bookmile.backend.global.jwt.JwtTokenProvider;
import com.bookmile.backend.global.jwt.handler.JwtExceptionHandlerFilter;
import com.bookmile.backend.global.oauth.CustomOAuth2UserService;
import com.bookmile.backend.global.oauth.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    // AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // token 사용 -> csrf 필요 없음
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(configurer -> configurer.configure(http))  // cors 활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안함
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/swagger-ui/**","swagger-ui/index.html#/","/v3/api-docs/**", "/swagger-resources/**").permitAll()
                        .requestMatchers("/api/v1/users/sign-up", "/api/v1/users/sign-in","/api/v1/users/reissue", "/api/v1/users/test/**").permitAll()
                        .requestMatchers("/api/v1/oauth2/test").permitAll()
                        .requestMatchers( "/oauth2/**", "/login/oauth2/code/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionHandlerFilter(), JwtAuthenticationFilter.class)
                .exceptionHandling(error-> error
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                        .accessDeniedHandler(new AccessDeniedHandlerImpl())
                )
        ;

        http
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(auth -> auth.baseUri("/oauth2/authorize"))
                        .redirectionEndpoint(redirect-> redirect.baseUri("/login/oauth2/code/*"))
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                        .successHandler(oAuth2SuccessHandler));

        return http.build();

    }


    @Bean
    public WebSecurityCustomizer ignoringCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**") // Swagger 관련 요청 무시
                .requestMatchers("/error", "/favicon.ico"); // oauth 시 리다이렉션 되는 url 경로 무시
    }

}