package com.bookmile.backend.domain.user.service;

import com.bookmile.backend.domain.user.dto.req.SignUpReqDto;
import com.bookmile.backend.domain.user.dto.res.UserResDto;

public interface UserService {
    UserResDto signUp(SignUpReqDto signUpReqDto);
}
