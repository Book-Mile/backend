package com.bookmile.backend.domain.user.service;

import com.bookmile.backend.domain.user.dto.req.PasswordReqDto;
import com.bookmile.backend.domain.user.dto.req.SignInReqDto;
import com.bookmile.backend.domain.user.dto.req.SignUpReqDto;
import com.bookmile.backend.domain.user.dto.req.UserInfoReqDto;
import com.bookmile.backend.domain.user.dto.res.TokenResDto;
import com.bookmile.backend.domain.user.dto.res.UserDetailResDto;
import com.bookmile.backend.domain.user.dto.res.UserResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UserService {
    UserResDto signUp(SignUpReqDto signUpReqDto);
    TokenResDto signIn(SignInReqDto signInReqDto);
    TokenResDto reIssue(HttpServletRequest request);
    UserDetailResDto getUser(String email);
    Boolean checkNickname(String nickname);

    void sendEmailCode(String email);
    void verificationCode( String email, String requestCode);
    TokenResDto updateUser(String email, UserInfoReqDto userInfoReqDto);
    void changePassword(String email, PasswordReqDto passwordReqDto);
    void updateProfile(String email, MultipartFile file);
    void deleteUser(String email);

    // 테스트 로그인
    TokenResDto testSignIn(SignInReqDto signInReqDto);
    Map<String, String> testRedirect(String accessToken);
}