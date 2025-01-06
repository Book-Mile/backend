package com.bookmile.backend.domain.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PasswordReqDto {

    @NotBlank(message = "비빌번호는 필수 입력 사항입니다.")
    @Schema(description = "기존 비밀번호", example = "oldPassword!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,16}$", message = "비밀번호는 8~16자의 영문 대소문자, 숫자, 특수문자로 이루어져야 합니다.")
    private String originPassword;

    @NotBlank(message = "비빌번호는 필수 입력 사항입니다.")
    @Schema(description = "변경 비밀번호", example = "newPassword!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,16}$", message = "비밀번호는 8~16자의 영문 대소문자, 숫자, 특수문자로 이루어져야 합니다.")
    private String newPassword;

    @NotBlank(message = "비빌번호 확인는 필수 입력 사항입니다.")
    @Schema(description = "변경 비밀번호 확인", example = "newPassword!")
    // 확인용이므로 Pattern 이 아니라, password 와 매치하기만 하면 됨.
    private String checkPassword;


}
