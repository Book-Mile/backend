package com.bookmile.backend.domain.user.service;

import com.bookmile.backend.domain.user.dto.req.SignInReqDto;
import com.bookmile.backend.domain.user.dto.req.SignUpReqDto;
import com.bookmile.backend.domain.user.dto.res.UserInfoDto;
import com.bookmile.backend.domain.user.dto.res.UserResDto;

public interface UserService {
    UserResDto signUp(SignUpReqDto signUpReqDto);

    UserResDto signIn(SignInReqDto signInReqDto);

    UserInfoDto getUserInfo(Long userId);
}
