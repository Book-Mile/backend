package com.bookmile.backend.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;

import static com.bookmile.backend.global.common.StatusCode.*;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    /*
        MethodArgumentNotValidException는 유효성 검사에서 실패하면 나타나는 예외로 bindingReult에 오류를 담는다.
        bindingResult가 없으면 400오류가 발생해 컨트롤러를 호출하지않고 오류페이지로 이동한다.
     */

    // 유효성 검사 실패로 인한 예외로 메시지와 오류 객체도 담는다.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e){
        ErrorResponse errorResponse= ErrorResponse.builder()
                .message(INPUT_VALUE_INVALID.getMessage())
                .validationErrors(ErrorResponse.ValidationError.from(e.getBindingResult()))
                .build();
        return ResponseEntity.status(INPUT_VALUE_INVALID.getStatus())
                .body(errorResponse);
    }

    // requestPart로 받은 multiPartFile이 null이면 발생하는 예외
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        ErrorResponse errorResponse= ErrorResponse.builder()
                .message(MULTI_PART_FILE_INVALID.getMessage())
                .build();
        return ResponseEntity.status(MULTI_PART_FILE_INVALID.getStatus())
                .body(errorResponse);
    }

    // 이미지 저장 시 생길 수 있는 예외, 주로 파일 입출력 오류 시 나타난다.
    @ExceptionHandler(IOException.class)  // 특정 예외를 처리
    public ResponseEntity<ErrorResponse> handleIOException(IOException e) {
        ErrorResponse errorResponse=ErrorResponse.builder()
                .message(FILE_INVALID.getMessage())
                .build();
        return ResponseEntity.status(FILE_INVALID.getStatus())
                .body(errorResponse);
    }

    // 무슨 예외가 터졌는지 메시지만 담는다.
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e){
        ErrorResponse errorResponse=ErrorResponse.builder()
                .message(e.getStatusCode().getMessage())
                .build();
        return ResponseEntity.status(e.getStatusCode().getStatus())
                .body(errorResponse);
    }
}
