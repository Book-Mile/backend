package com.bookmile.backend.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    /* User */
    SIGN_UP(CREATED, "회원가입이 완료되었습니다."),
    SIGN_IN(OK, "로그인에 성공하였습니다."),
    UPDATE_USER(OK, "회원 정보를 변경 완료"),
    USER_FOUND(OK, "회원 조회 완료"),
    GET_INFO(OK, "사용자 정보조회에 성공"),
    SEND_EMAIL_CODE(OK, "이메일 인증을 요청하였습니다."),

    /* Review */
    VIEW_REVIEW(OK, "리뷰가 조회 되었습니다."),
    CREATE_REVIEW(CREATED, "리뷰 생성이 되었습니다."),
    UPDATE_REVIEW(RESET_CONTENT, "리뷰 수정이 되었습니다."),
    DELETE_REVIEW(RESET_CONTENT, "리뷰 삭제가 되었습니다."),

    /* Record */
    VIEW_RECORD(OK, "기록이 조회 되었습니다."),
    CREATE_RECORD(CREATED, "기록 작성이 완료 되었습니다."),
    UPDATE_RECORD(RESET_CONTENT, "기록 수정이 완료 되었습니다."),

    /* Image */
    VIEW_IMAGE(OK, "이미지 조회가 되었습니다."),
    SAVE_IMAGE(CREATED, "이미지 저장이 되었습니다."),
    DELETE_IMAGE(RESET_CONTENT, "이미지 삭제가 되었습니다."),

    /* Book */
    BOOKLIST_SEARCH(OK, "책 리스트 조회에 성공했습니다."),
    BOOKDETAIL_SEARCH(OK, "책 상세 조회에 성공했습니다."),

    /* Group */
    GROUP_CREATE(CREATED, "그룹 생성에 성공했습니다."),

    /* TOKEN */
    ISSUED_TOKEN(OK,"토큰이 재발급되었습니다."),
    REFRESH_TOKEN_EXPIRED(UNAUTHORIZED,"Refresh-Token이 만료되었습니다"),
    ACCESS_TOKEN_EXPIRED(UNAUTHORIZED,"Access-Token이 만료되었습니다."),
    INVALID_TOKEN(UNAUTHORIZED,"유효하지 않은 JWT 토큰입니다."),
    FORBIDDEN_TOKEN(FORBIDDEN,"접근 권한이 없습니다."),
    TOKEN_NOT_FOUND(NOT_FOUND, "존재하는 토큰이 없습니다."),


    /* 400 BAD_REQUEST : 잘못된 요청 */
    PASSWORD_NOT_MATCH(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    PASSWORD_DUPLICATE(BAD_REQUEST, "이전 비밀번호와 동일합니다."),
    EMAIL_CODE_NOT_MATCH(BAD_REQUEST, "코드가 일지하지 않습니다."),

    /* 401 UNAUTHORIZED : 비인증 사용자 */
    AUTHENTICATION_FAILED(UNAUTHORIZED, "회원의 정보가 일치하지 않습니다."),
    AUTHENTICATION_FAILED_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다."),

    /* 403 FORBIDDEN : 권한 없음 */

    /* 404 NOT_FOUND : 존재하지 않는 리소스 */
    INPUT_VALUE_INVALID(NOT_FOUND, "유효하지 않은 입력입니다."),
    USER_NOT_FOUND(NOT_FOUND, "존재하는 회원이 없습니다."),
    BOOK_NOT_FOUND(NOT_FOUND, "존재하는 책이 없습니다."),
    REVIEW_NOT_FOUND(NOT_FOUND, "존재하는 리뷰가 없습니다."),
    GROUP_NOT_FOUND(NOT_FOUND, "존재하는 그룹이 없습니다."),
    RECORD_NOT_FOUND(NOT_FOUND, "존재하는 기록이 없습니다."),
    IMAGE_NOT_FOUND(NOT_FOUND, "존재하는 이미지가 없습니다."),
    NO_USER_OR_NO_GROUP(NOT_FOUND, "사용자 또는 그룹이 존재하지 않습니다."),
    PROVIDER_NOT_FOUND(NOT_FOUND, "존재하는 제공자가 없습니다."),

    /* 409 CONFLICT : 리소스 충돌 */
    USER_ALREADY_EXISTS(CONFLICT, "이미 존재하는 회원입니다."),

    /* 429 TOO_MANY_REQUESTS : 요청 과다 */
    EMAIL_TOO_MANY_REQUESTS(TOO_MANY_REQUESTS,"이메일 인증 요청 5번 초과로 더이상 인증 요청을 할 수 없습니다."),
    NICKNAME_TOO_MANY_REQUESTS(TOO_MANY_REQUESTS, "더이상 생성할 닉네임이 없습니다."),
    /* 500 INTERNAL_SERVER_ERROR  Error */
    REDIS_ERROR(INTERNAL_SERVER_ERROR, "Redis 서버에 연결할 수 없습니다. "),
    MAIL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "메일 서버에 오류가 생겼습니다.");


    private final HttpStatus status;
    private final String message;
}
