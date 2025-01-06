package com.bookmile.backend.domain.user.service;

import com.bookmile.backend.domain.user.dto.req.SignInReqDto;
import com.bookmile.backend.domain.user.dto.req.SignUpReqDto;
import com.bookmile.backend.domain.user.dto.res.SignInResDto;
import com.bookmile.backend.domain.user.dto.res.UserDetailResDto;
import com.bookmile.backend.domain.user.dto.res.UserInfoDto;
import com.bookmile.backend.domain.user.dto.res.UserResDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    UserResDto signUp(SignUpReqDto signUpReqDto);
    SignInResDto signIn(SignInReqDto signInReqDto);
    SignInResDto reIssue(HttpServletRequest request);
    UserInfoDto getUserInfo(Long userId);
    UserDetailResDto getUser(String email);
    Boolean checkNickname(String nickname);
}