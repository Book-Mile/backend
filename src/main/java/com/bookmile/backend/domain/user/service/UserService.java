package com.bookmile.backend.domain.user.service;

import com.bookmile.backend.domain.user.dto.req.PasswordReqDto;
import com.bookmile.backend.domain.user.dto.req.SignInReqDto;
import com.bookmile.backend.domain.user.dto.req.SignUpReqDto;
import com.bookmile.backend.domain.user.dto.req.UserInfoReqDto;
import com.bookmile.backend.domain.user.dto.res.TokenResDto;
import com.bookmile.backend.domain.user.dto.res.UserDetailResDto;
import com.bookmile.backend.domain.user.dto.res.UserInfoDto;
import com.bookmile.backend.domain.user.dto.res.UserResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserResDto signUp(SignUpReqDto signUpReqDto);
    TokenResDto signIn(SignInReqDto signInReqDto);
    TokenResDto reIssue(HttpServletRequest request);
    UserInfoDto getUserInfo(Long userId);
    UserDetailResDto getUser(String email);
    Boolean checkNickname(String nickname);

    void sendEmailCode(String email);
    void verificationCode( String email, String requestCode);
    TokenResDto updateUser(String email, UserInfoReqDto userInfoReqDto);
    void changePassword(String email, PasswordReqDto passwordReqDto);
    void updateProfile(String email, MultipartFile file);
    List<String> getOAuthProviders(String email);
    void deleteUser(String email);
    void unlinkUserOAuth(HttpServletRequest request, String provider, String email);
  //  void unlinkKakao(String email);
    // 테스트 로그인
    TokenResDto testSignIn(SignInReqDto signInReqDto);
    Map<String, String> testSocialLogin(String email);
    Map<String, String> testRedirect(String accessToken);
}