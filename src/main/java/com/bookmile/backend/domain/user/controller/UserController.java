package com.bookmile.backend.domain.user.controller;

import com.bookmile.backend.domain.user.dto.req.SignInReqDto;
import com.bookmile.backend.domain.user.dto.req.SignUpReqDto;
import com.bookmile.backend.domain.user.dto.res.UserResDto;
import com.bookmile.backend.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserResDto> signUp(@RequestBody @Valid SignUpReqDto signUpReqDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(signUpReqDto));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserResDto> signIn(@RequestBody @Valid SignInReqDto signInReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.signIn(signInReqDto));
    }
}

