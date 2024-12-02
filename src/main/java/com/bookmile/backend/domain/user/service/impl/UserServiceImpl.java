package com.bookmile.backend.domain.user.service.impl;

import com.bookmile.backend.domain.user.dto.req.SignUpReqDto;
import com.bookmile.backend.domain.user.dto.res.UserResDto;
import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.user.repository.UserRepository;
import com.bookmile.backend.domain.user.service.UserService;
import com.bookmile.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.bookmile.backend.global.common.StatusCode.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResDto signUp(SignUpReqDto signUpReqDto) {
        existsByEmail(signUpReqDto.getEmail());

        // 비밀번호 일치 확인
        if (!((signUpReqDto.getPassword()).equals(signUpReqDto.getCheckPassword()))){
            throw new CustomException(PASSWORD_NOT_MATCH);
        }
        User user = userRepository.save(signUpReqDto.toEntity(signUpReqDto.getEmail(), signUpReqDto.getPassword()));
        return UserResDto.toDto(user);
    }

    private void existsByEmail(String email) {
        if(userRepository.existsByEmail(email)){
            throw new CustomException(USER_ALREADY_EXISTS);
        };
    }
}
