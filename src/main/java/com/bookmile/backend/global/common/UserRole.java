package com.bookmile.backend.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    GUEST("ROLE_GUEST","게스트"),
    USER("ROLE_USER", "일반회원"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String role;
    private final String title;
}
