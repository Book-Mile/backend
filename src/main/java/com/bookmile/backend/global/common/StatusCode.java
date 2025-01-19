package com.bookmile.backend.global.common;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.RESET_CONTENT;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;
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
    UPDATE_USER(OK, "회원 정보를 변경 완료"),
    USER_FOUND(OK, "회원 조회 완료"),
    SEND_EMAIL_CODE(OK, "이메일 인증을 요청하였습니다."),
    USER_DELETE(NO_CONTENT, "회원 정보 삭제 완료"),

    /* Review */
    VIEW_REVIEW(OK, "리뷰가 조회 되었습니다."),
    CREATE_REVIEW(CREATED, "리뷰 생성이 되었습니다."),
    UPDATE_REVIEW(RESET_CONTENT, "리뷰 수정이 되었습니다."),
    DELETE_REVIEW(RESET_CONTENT, "리뷰 삭제가 되었습니다."),
    VIEW_BOOK_REVIEW_RATE(OK, "해당 책의 리뷰 전체 평점이 조회 되었습니다."),

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
    GROUP_JOIN(OK, "그룹에 참여하였습니다."),
    ALREADY_JOINED_GROUP(BAD_REQUEST, "이미 해당 그룹에 참여 중입니다."),
    GROUP_MEMBER_LIMIT_REACHED(BAD_REQUEST, "그룹의 최대 인원수를 초과했습니다."),
    INVALID_GROUP_PASSWORD(BAD_REQUEST, "그룹 비밀번호가 유효하지 않습니다."),
    CHECKPOINT_TEMPLETE(OK, "템플릿 조회에 성공했습니다."),

    /* TOKEN */
    ISSUED_TOKEN(OK, "토큰이 재발급되었습니다."),
    REFRESH_TOKEN_EXPIRED(UNAUTHORIZED, "Refresh-Token이 만료되었습니다"),
    ACCESS_TOKEN_EXPIRED(UNAUTHORIZED, "Access-Token이 만료되었습니다."),
    INVALID_TOKEN(UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
    FORBIDDEN_TOKEN(FORBIDDEN, "접근 권한이 없습니다."),
    TOKEN_NOT_FOUND(NOT_FOUND, "존재하는 토큰이 없습니다."),


    /* 400 BAD_REQUEST : 잘못된 요청 */
    PASSWORD_NOT_MATCH(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    PASSWORD_DUPLICATE(BAD_REQUEST, "이전 비밀번호와 동일합니다."),
    EMAIL_CODE_NOT_MATCH(BAD_REQUEST, "코드가 일지하지 않습니다."),
    FILE_SAVE_INVALID(BAD_REQUEST, "파일 저장 중 오류가 발생했습니다."),
    FILE_DELETE_INVALID(BAD_REQUEST, "파일 삭제 중 오류가 발생했습니다."),
    FILE_CHANGE_INVALID(BAD_REQUEST, "MultiPart 파일 변환 중 오류가 발생했습니다."),
    CUSTOM_GOAL_REQUIRED(BAD_REQUEST, "GoalType이 CUSTOM일 경우 사용자 정의 목표(customGoal)는 필수입니다."),
    MULTI_PART_FILE_INVALID(BAD_REQUEST,"유효하지 않은 MultiPartFile입니다."),
    FILE_INVALID(BAD_REQUEST,"파일 저장 중 오류가 발생했습니다."),

    /* 401 UNAUTHORIZED : 비인증 사용자 */
    AUTHENTICATION_FAILED(UNAUTHORIZED, "회원의 정보가 일치하지 않습니다."),
    AUTHENTICATION_FAILED_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다."),

    /* 403 FORBIDDEN : 권한 없음 */

    /* 404 NOT_FOUND : 존재하지 않는 리소스 */
    INPUT_VALUE_INVALID(NOT_FOUND, "유효하지 않은 입력입니다."),
    INVALID_GROUP_ID(NOT_FOUND, "그룹 ID가 유효하지 않습니다."),
    USER_NOT_FOUND(NOT_FOUND, "존재하는 회원이 없습니다."),
    BOOK_NOT_FOUND(NOT_FOUND, "존재하는 책이 없습니다."),
    REVIEW_NOT_FOUND(NOT_FOUND, "존재하는 리뷰가 없습니다."),
    GROUP_NOT_FOUND(NOT_FOUND, "존재하는 그룹이 없습니다."),
    RECORD_NOT_FOUND(NOT_FOUND, "존재하는 기록이 없습니다."),
    IMAGE_NOT_FOUND(NOT_FOUND, "존재하는 이미지가 없습니다."),
    NO_USER_OR_NO_GROUP(NOT_FOUND, "사용자 또는 그룹이 존재하지 않습니다."),
    PROVIDER_NOT_FOUND(NOT_FOUND, "존재하는 제공자가 없습니다."),
    INVALID_FILE_TYPE(NOT_FOUND, "유효하지 않은 파일 형식입니다."),
    INVALID_GOAL_TYPE(NOT_FOUND, "유효하지 않은 GoalType 값입니다"),
    INVALID_TEMPLATE_ID(NOT_FOUND, "존재하지 않는 템플릿입니다."),
    BOOK_INFO_NOT_FOUND(NOT_FOUND, "책 정보를 가져올 수 없습니다."),

    /* 409 CONFLICT : 리소스 충돌 */
    USER_ALREADY_EXISTS(CONFLICT, "이미 존재하는 회원입니다."),

    /* 429 TOO_MANY_REQUESTS : 요청 과다 */
    EMAIL_TOO_MANY_REQUESTS(TOO_MANY_REQUESTS, "이메일 인증 요청 5번 초과로 더이상 인증 요청을 할 수 없습니다."),
    NICKNAME_TOO_MANY_REQUESTS(TOO_MANY_REQUESTS, "더이상 생성할 닉네임이 없습니다."),

    /* 500 INTERNAL_SERVER_ERROR  Error */
    REDIS_ERROR(INTERNAL_SERVER_ERROR, "Redis 서버에 연결할 수 없습니다. "),
    MAIL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "메일 서버에 오류가 생겼습니다."),
    INVALID_TEMPLATE_USAGE(NOT_FOUND,"완독한 그룹의 템플릿만 사용할 수 있습니다."),
    GOAL_CONTENT_REQUIRED(NOT_FOUND,"목표 상세 내용은 필수입니다." ),
    NO_PERMISSION(UNAUTHORIZED,"그룹장만 그룹의 상태를 변경할 수 있습니다."),
    NOT_MEMBER(UNAUTHORIZED,"그룹 구성원이 아닙니다." ),
    INVALID_GROUP_STATUS_UPDATE(BAD_REQUEST,"잘못된 요청입니다."),
    INVALID_GROUP(BAD_REQUEST,"유효하지 않은 그룹입니다." ),
    INVALID_BOOK_ID(BAD_REQUEST,"존재하지 않는 책입니다." ),

    BESTSELLER_SEARCH(OK,"베스트 셀러 조회에 성공했습니다." ),
    NEWBOOK_SEARCH(OK,"신간 도서 조회에 성공했습니다." ),
    GROUP_LIST_FOUND(OK, "그룹 리스트 조회에 성공했습니다."),
    GROUP_PRIVATE_UPDATE(OK, "그룹 공개 여부가 업데이트 되었습니다.");

    private final HttpStatus status;
    private final String message;
}
