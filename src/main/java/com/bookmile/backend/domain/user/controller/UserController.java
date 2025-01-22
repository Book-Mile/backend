package com.bookmile.backend.domain.user.controller;

import com.bookmile.backend.domain.user.dto.req.*;
import com.bookmile.backend.domain.user.dto.res.TokenResDto;
import com.bookmile.backend.domain.user.dto.res.UserDetailResDto;
import com.bookmile.backend.domain.user.dto.res.UserResDto;
import com.bookmile.backend.domain.user.service.UserService;
import com.bookmile.backend.global.common.CommonResponse;
import com.bookmile.backend.global.oauth.OAuth2UnlinkService;
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

import java.util.List;
import java.util.Map;

import static com.bookmile.backend.global.common.StatusCode.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입", description = "소셜로그인이 아닌 '일반 회원가입'으로 가입하는 API 입니다. <br>" +
            "닉네임은 입력하실 필요 없습니다. 초기 회원가입시, 닉네임은 랜덤생성되어 회원정보가 저장됩니다. ex)기운찬고양이23 ")
    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponse<UserResDto>> signUp(@RequestBody @Valid SignUpReqDto signUpReqDto) {
        return ResponseEntity.status(SIGN_UP.getStatus())
                .body(CommonResponse.from(SIGN_UP.getMessage(), userService.signUp(signUpReqDto)));
    }

    @Operation(summary = "로그인", description = "'일반 로그인'으로 로그인합니다. <br>" +
            "accessToken과 refreshToken이 body를 통해서 전달됩니다.")
    @PostMapping("/sign-in")
    public ResponseEntity<CommonResponse<TokenResDto>> signIn(@RequestBody @Valid SignInReqDto signInReqDto) {
        return ResponseEntity.status(SIGN_IN.getStatus())
                .body(CommonResponse.from(SIGN_IN.getMessage(), userService.signIn(signInReqDto)));
    }

    @Operation(summary = "토큰 재발급", description = "Header 에 refreshToken을 담아 요청을 보내야 합니다.")
    @PostMapping("/reissue")
    public ResponseEntity<CommonResponse<TokenResDto>> reissue(HttpServletRequest request) {
        return ResponseEntity.ok(CommonResponse.from(ISSUED_TOKEN.getMessage(), userService.reIssue(request)));
    }

    @Operation(summary = "회원 정보 조회", description = "access token을 이용하여 회원 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonResponse<UserDetailResDto>> getUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(CommonResponse.from(USER_FOUND.getMessage(), userService.getUser(userDetails.getUsername())));
    }

    @Operation(summary = "닉네임 중복확인", description = "true : 닉네임 중복 , false : 사용 가능")
    @PostMapping("/exists")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
        return ResponseEntity.ok(userService.checkNickname(nickname));
    }

    @Operation(summary = "닉네임 수정 및 회원 정보 변경", description = "닉네임 중복확인을 거친후, 최종 업데이트를 할 때 사용가능합니다. <br>" +
            "이메일 또한, 인증 완료후 사용해주세요. 본 APi는 최종 정보를 반영합니다. <br>" +
            "!! 이메일 변경으로 인해 기존 토큰정보와 달라지므로, 토큰이 재발급됩니다. <br> " +
            "따라서, '재로그인'을 유도해야 합니다.")

    @PutMapping
    public ResponseEntity<CommonResponse<TokenResDto>> updateUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserInfoReqDto userInfoReqDto) {
        return ResponseEntity.ok(CommonResponse.from(UPDATE_USER.getMessage(),
                userService.updateUser(userDetails.getUsername(), userInfoReqDto)));
    }

    @Operation(summary = "이메일 인증 코드 전송", description = "이메일 인증을 위한 6자리 난수를 보내는 코드입니다. <br>" +
            "이메일 1개당, 하루 최대 5번의 인증만 가능합니다. <br>" +
            "해당 이메일의 메일을 통해 코드가 전달됩니다.")
    @PostMapping("/email")
    public ResponseEntity<CommonResponse<Object>> sendEmailCode(@RequestBody @Valid EmailReqDto emailReqDto) {
        userService.sendEmailCode(emailReqDto.getEmail());
        return ResponseEntity.ok(CommonResponse.from(SEND_EMAIL_CODE.getMessage()));
    }

    @Operation(summary = "이메일 코드 인증", description = "작성한 이메일과 6자리 코드가 서버에서 제공한 이메일과 일치하는지 확인합니다.<br>" +
            "인증 코드가 확인되면, 작성한 이메일로 회원정보가 수정됩니다. <br>" +
            "단, 인증 코드는 5분간 유효합니다.")
    @PostMapping("/email/verify")
    public ResponseEntity<CommonResponse<Object>> verifyEmailCode(
            @RequestBody @Valid EmailCodeReqDto emailCodeReqDto) {
        userService.verificationCode(emailCodeReqDto.getEmail(), emailCodeReqDto.getCode());
        return ResponseEntity.ok(CommonResponse.from(UPDATE_USER.getMessage()));
    }

    @Operation(summary = "비밀번호 변경", description = "3가지의 비밀번호 형식을 맞춰주셔야 합니다. 아래는 3가지의 검증을 거칩니다. <br>" +
            "1. 기존 비밀번호(originPassword)를 확인합니다.<br>" +
            "2. 기존비밀번호와 변경하려는 비밀번호(newPassword)가 다른지 확인합니다. <br>" +
            "3. 변경 비밀번호와 확인용 비밀번호(checkPassword)가 일치한지 확인합니다. ")
    @PutMapping("/password")
    public ResponseEntity<CommonResponse<Object>> updatePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid PasswordReqDto passwordReqDto) {
        userService.changePassword(userDetails.getUsername(), passwordReqDto);
        return ResponseEntity.ok(CommonResponse.from(UPDATE_USER.getMessage()));
    }

    @Operation(summary = "프로필 이미지 수정", description = "프로필 이미지를 수정합니다. <br>" +
            "파일의 형식은 '.png, .jpeg, .jpg'의 파일 확장자만 가능합니다.")
    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse<Object>> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart(value = "file") MultipartFile file) {
        userService.updateProfile(userDetails.getUsername(), file);
        return ResponseEntity.ok(CommonResponse.from(UPDATE_USER.getMessage()));
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping
    public ResponseEntity<CommonResponse<Object>> deleteUser(
            @AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(userDetails.getUsername());
        return ResponseEntity.status(USER_DELETE.getStatus()).body(CommonResponse.from(USER_DELETE.getMessage()));
    }

    @Operation(summary ="[테스트] 로그인", description = "토큰 만료 시간을 짧게 하여, refreshToken을 통해 자동 로그인이 이루어지도록 개발한 테스트 확인용 API입니다. <br>" +
            "accessToken : 30초, refreshToken : 1분입니다.")
    @PostMapping("/test/sign-in")
    public ResponseEntity<CommonResponse<TokenResDto>> testSignIn(
            @RequestBody @Valid SignInReqDto signInReqDto
    ){
         return ResponseEntity.status(SIGN_IN.getStatus())
                 .body(CommonResponse.from(SIGN_IN.getMessage(),userService.testSignIn(signInReqDto)));
    }

    @Operation(summary = "[테스트] OAuth2 로그인 (소셜로그인)", description = "테스트용입니다. <br>" +
            "토큰의 유효시간이 매우 짧습니다. accessToken : 30초, refreshToken : 1분 <br>" +
            "회원가입 따로 없이, test용 email을 입력하여 계정을 생성하고, redirectUrl이 올바르게 나오는지 확인합니다. <br>" +
            "또한, 제공한 accessToken, refreshToken을 유효한지 확인합니다. ")
    @PostMapping("/test/oauth2")
    public ResponseEntity<CommonResponse<Map<String, String>>> testSocialLogin(
            @RequestParam String email
    ) {
        return ResponseEntity.status(SIGN_IN.getStatus())
                .body(CommonResponse.from(SIGN_IN.getMessage(), userService.testSocialLogin(email)));
    }

    @Operation(summary = "[테스트] 토큰 확인용",description = "리다이렉트 url에 토큰이 올바른지 검사합니다. <br>" +
            "유저의 정보를 확인합니다.")
    @PostMapping("/test/redirect")
    public ResponseEntity<CommonResponse<Map<String, String>>> testRedirect(
            @RequestBody TestAccessReqDto testAccessReqDto
    ){
        return ResponseEntity.status(SIGN_IN.getStatus())
                .body(CommonResponse.from(SIGN_IN.getMessage(), userService.testRedirect(testAccessReqDto.getAccessToken())));
    }

    @Operation(summary = "연동된 소셜 로그인 조회", description = "현재 로그인한 사용자의 연동된 소셜 로그인 목록을 조회합니다.")
    @GetMapping("/oauth2")
    public ResponseEntity<CommonResponse<List<String>>> getOAuthProviders(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(CommonResponse.from(USER_FOUND.getMessage(),  userService.getOAuthProviders(userDetails.getUsername())));
    }

    @Operation(summary = "소셜로그인 연동 해제", description = "소셜 로그인 연동을 해제합니다. ")
    @PostMapping("/oauth2/unlink/{provider}")
    public ResponseEntity<CommonResponse<Object>> unlink(
            HttpServletRequest request,
            @PathVariable(name = "provider") String provider,
            @AuthenticationPrincipal UserDetails userDetails) {
        userService.unlinkUserOAuth(request, provider, userDetails.getUsername());

        return ResponseEntity.ok(CommonResponse.from("연동해제 성공"));

    }
}

