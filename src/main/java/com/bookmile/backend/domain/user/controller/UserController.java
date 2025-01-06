package com.bookmile.backend.domain.user.controller;

import com.bookmile.backend.domain.user.dto.req.*;
import com.bookmile.backend.domain.user.dto.res.SignInResDto;
import com.bookmile.backend.domain.user.dto.res.UserDetailResDto;
import com.bookmile.backend.domain.user.dto.res.UserResDto;
import com.bookmile.backend.domain.user.service.UserService;
import com.bookmile.backend.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.bookmile.backend.global.common.StatusCode.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입", description = "소셜로그인이 아닌 '일반 로그인'으로 가입하는 API 입니다.")
    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponse<UserResDto>> signUp(@RequestBody @Valid SignUpReqDto signUpReqDto) {
        return ResponseEntity.status(SIGN_UP.getStatus())
                .body(CommonResponse.from(SIGN_UP.getMessage(),userService.signUp(signUpReqDto)));
    }

    @Operation(summary = "로그인", description = "'일반 로그인'으로 로그인합니다.")
    @PostMapping("/sign-in")
    public ResponseEntity<CommonResponse<SignInResDto>> signIn(@RequestBody @Valid SignInReqDto signInReqDto) {
        return ResponseEntity.status(SIGN_IN.getStatus())
                .body(CommonResponse.from(SIGN_IN.getMessage(),userService.signIn(signInReqDto)));
    }

    @Operation(summary = "토큰 재발급", description = "Header 에 refreshToken을 담아 요청을 보내야 합니다.")
    @PostMapping("/reissue")
    public ResponseEntity<CommonResponse<SignInResDto>> reissue(HttpServletRequest request) {
        return ResponseEntity.ok(CommonResponse.from(ISSUED_TOKEN.getMessage(), userService.reIssue(request)));
    }

    @Operation(summary = "회원 정보 조회", description = "access token을 이용하여 회원 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonResponse<UserDetailResDto>> getUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(CommonResponse.from(USER_FOUND.getMessage(), userService.getUser(userDetails.getUsername())));
    }

    @Operation(summary = "닉네임 중복확인", description = "true : 닉네임 중복 , false : 사용 가능")
    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
        return ResponseEntity.ok(userService.checkNickname(nickname));
    }

    @PostMapping("/email")
    public ResponseEntity<CommonResponse<Object>> sendEmailCode(@RequestBody @Valid EmailReqDto emailReqDto) {
        userService.sendEmailCode(emailReqDto.getEmail());
        return ResponseEntity.ok(CommonResponse.from(SEND_EMAIL_CODE.getMessage()));
    }

    @PostMapping("/email/verify")
    public ResponseEntity<Boolean> verifyEmailCode(@RequestBody @Valid EmailCodeReqDto emailCodeReqDto ) {
        Boolean result = userService.verificationCode(emailCodeReqDto.getEmail(), emailCodeReqDto.getCode());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/password")
    public ResponseEntity<CommonResponse<Object>> updatePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid PasswordReqDto passwordReqDto) {
        userService.changePassword(userDetails.getUsername(), passwordReqDto);
        return ResponseEntity.ok(CommonResponse.from(UPDATE_USER.getMessage()));
    }

    @PutMapping(value = "/profile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse<Object>> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart(value = "file") MultipartFile file){
        userService.updateProfile(userDetails.getUsername(), file);
        return ResponseEntity.ok(CommonResponse.from(UPDATE_USER.getMessage()));
    }
}

