package com.bookmile.backend.domain.user.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInResDto {
    private String accessToken;
    private String refreshToken;

    @Builder
    private SignInResDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static SignInResDto toDto(String accessToken, String refreshToken) {
        return SignInResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
