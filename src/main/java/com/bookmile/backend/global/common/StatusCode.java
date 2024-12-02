package com.bookmile.backend.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.CONFLICT;

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    PASSWORD_NOT_MATCH(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    /* 401 UNAUTHORIZED : 비인증 사용자 */

    /* 403 FORBIDDEN : 권한 없음 */

    /* 404 NOT_FOUND : 존재하지 않는 리소스 */
    INPUT_VALUE_INVALID(NOT_FOUND, "유효하지 않은 입력입니다."),
    /* 409 CONFLICT : 리소스 충돌 */
    USER_ALREADY_EXISTS(CONFLICT, "이미 존재하는 회원입니다.");

    private final HttpStatus status;
    private final String message;
}
