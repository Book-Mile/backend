package com.bookmile.backend.domain.user.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenResDto {
    private String accessToken;
    private String refreshToken;

    @Builder
    private TokenResDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static TokenResDto toDto(String accessToken, String refreshToken) {
        return TokenResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
