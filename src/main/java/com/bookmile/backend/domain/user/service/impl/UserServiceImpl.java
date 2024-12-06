package com.bookmile.backend.domain.user.service.impl;

import com.bookmile.backend.domain.user.dto.req.SignInReqDto;
import com.bookmile.backend.domain.user.dto.req.SignUpReqDto;
import com.bookmile.backend.domain.user.dto.res.UserResDto;
import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.user.repository.UserRepository;
import com.bookmile.backend.domain.user.service.UserService;
import com.bookmile.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.bookmile.backend.global.common.StatusCode.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
    public UserResDto signIn(SignInReqDto signInReqDto) {
        User user = findByEmail(signInReqDto.getEmail());

        if(!passwordEncoder.matches(signInReqDto.getPassword(), user.getPassword()))
            throw new CustomException(AUTHENTICATION_FAILED); // 유저는 아이디, 비밀번호 중 한개만 틀려도 '일치하는 정보가 없음' 메세지 표시

        return UserResDto.toDto(user);
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


