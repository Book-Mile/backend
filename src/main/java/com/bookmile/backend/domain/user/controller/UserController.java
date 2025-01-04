package com.bookmile.backend.domain.user.controller;

import static com.bookmile.backend.global.common.StatusCode.GET_INFO;
import static com.bookmile.backend.global.common.StatusCode.SIGN_IN;
import static com.bookmile.backend.global.common.StatusCode.SIGN_UP;

import com.bookmile.backend.domain.user.dto.req.SignInReqDto;
import com.bookmile.backend.domain.user.dto.req.SignUpReqDto;
import com.bookmile.backend.domain.user.dto.res.UserInfoDto;
import com.bookmile.backend.domain.user.dto.res.UserResDto;
import com.bookmile.backend.domain.user.service.UserService;
import com.bookmile.backend.global.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<CommonResponse<UserResDto>> signUp(@RequestBody @Valid SignUpReqDto signUpReqDto) {
        return ResponseEntity.status(SIGN_UP.getStatus())
                .body(CommonResponse.from(SIGN_UP.getMessage(), userService.signUp(signUpReqDto)));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<CommonResponse<UserResDto>> signIn(@RequestBody @Valid SignInReqDto signInReqDto) {
        return ResponseEntity.status(SIGN_IN.getStatus())
                .body(CommonResponse.from(SIGN_IN.getMessage(), userService.signIn(signInReqDto)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CommonResponse<UserInfoDto>> getUserInfo(@PathVariable Long userId) {
        return ResponseEntity.status(GET_INFO.getStatus())
                .body(CommonResponse.from(GET_INFO.getMessage(), userService.getUserInfo(userId)));
    }
}

