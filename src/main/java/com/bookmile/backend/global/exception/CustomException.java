package com.bookmile.backend.global.exception;

import com.bookmile.backend.global.common.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final StatusCode statusCode;
}
