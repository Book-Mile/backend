package com.bookmile.backend.domain.user.service;

import com.bookmile.backend.domain.user.dto.req.PasswordReqDto;
import com.bookmile.backend.domain.user.dto.req.SignInReqDto;
import com.bookmile.backend.domain.user.dto.req.SignUpReqDto;
import com.bookmile.backend.domain.user.dto.res.SignInResDto;
import com.bookmile.backend.domain.user.dto.res.UserDetailResDto;
import com.bookmile.backend.domain.user.dto.res.UserInfoDto;
import com.bookmile.backend.domain.user.dto.res.UserResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserResDto signUp(SignUpReqDto signUpReqDto);
    SignInResDto signIn(SignInReqDto signInReqDto);
    SignInResDto reIssue(HttpServletRequest request);
    UserInfoDto getUserInfo(Long userId);
    UserDetailResDto getUser(String email);
    Boolean checkNickname(String nickname);

    void sendEmailCode(String email);
    Boolean verificationCode(String email, String requestCode);

    void changePassword(String email, PasswordReqDto passwordReqDto);
    void updateProfile(String email, MultipartFile file);
    void deleteUser(String email);
}