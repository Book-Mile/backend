package com.bookmile.backend.domain.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EmailReqDto {

    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    @Schema(description = "인증할 이메일 주소", example = "user@example.com")
    @Pattern(regexp="^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+[.][a-zA-Z]{2,3}$", message="이메일 형식이 올바르지 않습니다.")
    private String email;
}
