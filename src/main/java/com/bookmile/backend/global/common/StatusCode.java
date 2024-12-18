package com.bookmile.backend.global.common;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.RESET_CONTENT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    /* User */
    SIGN_UP(CREATED, "회원가입이 완료되었습니다."),
    SIGN_IN(OK, "로그인에 성공하였습니다."),

    /* Review */
    VIEW_REVIEW(OK, "리뷰가 조회 되었습니다."),
    CREATE_REVIEW(CREATED, "리뷰 생성이 되었습니다."),
    UPDATE_REVIEW(RESET_CONTENT, "리뷰 수정이 되었습니다."),
    DELETE_REVIEW(RESET_CONTENT, "리뷰 삭제가 되었습니다."),

    /* Record */
    VIEW_RECORD(OK, "기록이 조회 되었습니다."),
    CREATE_RECORD(CREATED, "기록 작성이 완료 되었습니다."),
    UPDATE_RECORD(RESET_CONTENT, "기록 수정이 완료 되었습니다."),


    /* 400 BAD_REQUEST : 잘못된 요청 */
    PASSWORD_NOT_MATCH(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    /* 401 UNAUTHORIZED : 비인증 사용자 */
    AUTHENTICATION_FAILED(UNAUTHORIZED, "회원의 정보가 일치하지 않습니다."),

    /* 403 FORBIDDEN : 권한 없음 */

    /* 404 NOT_FOUND : 존재하지 않는 리소스 */
    INPUT_VALUE_INVALID(NOT_FOUND, "유효하지 않은 입력입니다."),
    USER_NOT_FOUND(NOT_FOUND, "존재하는 회원이 없습니다."),
    BOOK_NOT_FOUND(NOT_FOUND, "존재하는 책이 없습니다."),
    REVIEW_NOT_FOUND(NOT_FOUND, "존재하는 리뷰가 없습니다."),
    GROUP_NOT_FOUND(NOT_FOUND, "존재하는 그룹이 없습니다."),
    RECORD_NOT_FOUND(NOT_FOUND, "존재하는 기록이 없습니다."),
    NO_USER_OR_NO_GROUP(NOT_FOUND, "사용자 또는 그룹이 존재하지 않습니다."),

    /* 409 CONFLICT : 리소스 충돌 */
    USER_ALREADY_EXISTS(CONFLICT, "이미 존재하는 회원입니다.");

    private final HttpStatus status;
    private final String message;
}
