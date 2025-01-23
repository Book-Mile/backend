package com.bookmile.backend.domain.user.controller;

import com.bookmile.backend.domain.user.service.OAuthService;
import com.bookmile.backend.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.bookmile.backend.global.common.StatusCode.SIGN_IN;
import static com.bookmile.backend.global.common.StatusCode.USER_FOUND;

@RestController
@RequestMapping("api/v1/oauth2")
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oAuthService;

    @Operation(summary = "[테스트] OAuth2 로그인 (소셜로그인)", description = "테스트용입니다. <br>" +
            "토큰의 유효시간이 매우 짧습니다. accessToken : 30초, refreshToken : 1분 <br>" +
            "회원가입 따로 없이, test용 email을 입력하여 계정을 생성하고, redirectUrl이 올바르게 나오는지 확인합니다. <br>" +
            "또한, 제공한 accessToken, refreshToken을 유효한지 확인합니다. ")
    @PostMapping("/test")
    public ResponseEntity<CommonResponse<Map<String, String>>> testSocialLogin(
            @RequestParam String email
    ) {
        return ResponseEntity.status(SIGN_IN.getStatus())
                .body(CommonResponse.from(SIGN_IN.getMessage(), oAuthService.testSocialLogin(email)));
    }

    @Operation(summary = "연동된 소셜 로그인 조회", description = "현재 로그인한 사용자의 연동된 소셜 로그인 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonResponse<List<String>>> getOAuthProviders(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(CommonResponse.from(USER_FOUND.getMessage(), oAuthService.getOAuthProviders(userDetails.getUsername())));
    }

    @Operation(summary = "소셜로그인 연동 해제", description = "소셜 로그인 연동을 해제합니다. <br>" +
            "provider에는 kakao, google, naver 중 1개 입력해주세요. <br>" +
            "Header에 accessToken 필요합니다!")
    @PostMapping( "unlink/{provider}")
    public ResponseEntity<CommonResponse<Object>> unlinkOAuthProvider(
            HttpServletRequest request,
            @PathVariable(value = "provider") String provider,
            @AuthenticationPrincipal UserDetails userDetails) {
        oAuthService.unlinkUserOAuth(request, provider, userDetails.getUsername());

        return ResponseEntity.ok(CommonResponse.from("연동해제 성공"));
    }
}
