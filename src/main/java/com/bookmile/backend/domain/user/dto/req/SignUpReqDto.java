package com.bookmile.backend.domain.user.dto.req;

import com.bookmile.backend.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpReqDto {


    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    @Schema(description = "회원의 이메일 주소", example = "user@example.com")
    @Pattern(regexp="^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+[.][a-zA-Z]{2,3}$", message="이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비빌번호는 필수 입력 사항입니다.")
    @Schema(description = "회원의 비밀번호", example = "userPassword!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,16}$", message = "비밀번호는 8~16자의 영문 대소문자, 숫자, 특수문자로 이루어져야 합니다.")
    private String password;

    @NotBlank(message = "비빌번호 확인는 필수 입력 사항입니다.")
    @Schema(description = "회원의 비밀번호 확인", example = "userPassword!")
    // 확인용이므로 Pattern 이 아니라, password 와 매치하기만 하면 됨.
    private String checkPassword;


    public User toEntity(String email, String password) {
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }
}
