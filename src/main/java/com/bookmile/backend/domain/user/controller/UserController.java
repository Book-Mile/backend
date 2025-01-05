package com.bookmile.backend.domain.user.controller;

import com.bookmile.backend.domain.user.dto.req.SignInReqDto;
import com.bookmile.backend.domain.user.dto.req.SignUpReqDto;
import com.bookmile.backend.domain.user.dto.res.SignInResDto;
import com.bookmile.backend.domain.user.dto.res.UserResDto;
import com.bookmile.backend.domain.user.service.UserService;
import com.bookmile.backend.global.common.CommonResponse;
import com.bookmile.backend.global.redis.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.bookmile.backend.global.common.StatusCode.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponse<UserResDto>> signUp(@RequestBody @Valid SignUpReqDto signUpReqDto) {
        return ResponseEntity.status(SIGN_UP.getStatus())
                .body(CommonResponse.from(SIGN_UP.getMessage(),userService.signUp(signUpReqDto)));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<CommonResponse<SignInResDto>> signIn(@RequestBody @Valid SignInReqDto signInReqDto) {
        return ResponseEntity.status(SIGN_IN.getStatus())
                .body(CommonResponse.from(SIGN_IN.getMessage(),userService.signIn(signInReqDto)));
    }

    @PostMapping("/reissue")
    public ResponseEntity<CommonResponse<SignInResDto>> reissue(HttpServletRequest request) {
        return ResponseEntity.ok(CommonResponse.from(ISSUED_TOKEN.getMessage(), userService.reIssue(request)));
    }

}

