package com.bookmile.backend.domain.user.service.impl;

import com.bookmile.backend.domain.user.dto.req.SignInReqDto;
import com.bookmile.backend.domain.user.dto.req.SignUpReqDto;
import com.bookmile.backend.domain.user.dto.res.SignInResDto;
import com.bookmile.backend.domain.user.dto.res.UserDetailResDto;
import com.bookmile.backend.domain.user.dto.res.UserResDto;
import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.user.repository.UserRepository;
import com.bookmile.backend.domain.user.service.UserService;
import com.bookmile.backend.global.exception.CustomException;
import com.bookmile.backend.global.jwt.JwtTokenProvider;
import com.bookmile.backend.global.redis.RefreshToken;
import com.bookmile.backend.global.redis.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bookmile.backend.global.common.StatusCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public UserResDto signUp(SignUpReqDto signUpReqDto) {
        existsByEmail(signUpReqDto.getEmail());

        // 비밀번호 일치 여부 확인
        if (!(signUpReqDto.getPassword().equals(signUpReqDto.getCheckPassword())))
            throw new CustomException(PASSWORD_NOT_MATCH);

        String enCodePassword = passwordEncoder.encode(signUpReqDto.getPassword());

        User user = userRepository.save(signUpReqDto.toEntity(signUpReqDto.getEmail(), enCodePassword));
        return UserResDto.toDto(user);
    }

    @Override
    @Transactional
    public SignInResDto signIn(SignInReqDto signInReqDto) {
         User user = findByEmail(signInReqDto.getEmail());

         if(!passwordEncoder.matches(signInReqDto.getPassword(), user.getPassword()))
            throw new CustomException(AUTHENTICATION_FAILED); // 유저는 아이디, 비밀번호 중 한개만 틀려도 '일치하는 정보가 없음' 메세지 표시

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getId(), user.getRole().toString());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getId());

        return SignInResDto.toDto(accessToken, refreshToken);
    }

    @Override
    public SignInResDto reIssue(HttpServletRequest request) {

        String token = jwtTokenProvider.resolveToken(request);

        if(token == null) {
            throw new CustomException(INVALID_TOKEN);
        }

        Long userId = jwtTokenProvider.getUserId(token);
        log.info("refreshToken : {}, userId: {}", token, userId);

        RefreshToken refreshToken = refreshTokenRepository.findById("refreshToken" + userId).orElseThrow(() -> new CustomException(TOKEN_NOT_FOUND));
        log.info("Redis RefreshToken : {}", refreshToken.getRefreshToken());

        if(!refreshToken.getRefreshToken().equals(token)) {
            throw new CustomException(INVALID_TOKEN);
        }

        User user = userRepository.findById(userId).orElseThrow(()-> new CustomException(USER_NOT_FOUND));

        String newAccessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getId(), user.getRole().toString());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getId());

        SignInResDto signInResDto = SignInResDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();

        return signInResDto;
    }

    @Override
    public UserDetailResDto getUser(String email) {
        log.info("UserServiceImpl.getUser: {}", email);
        User user = findByEmail(email);
        return UserDetailResDto.toDto(user);
    }

    private void existsByEmail(String email) {
        if(userRepository.existsByEmail(email)){
            throw new CustomException(USER_ALREADY_EXISTS);
        };
    }

    private User findByEmail(String email) {
        return  userRepository.findByEmail(email).orElseThrow(() -> new CustomException(AUTHENTICATION_FAILED));
    }
}


