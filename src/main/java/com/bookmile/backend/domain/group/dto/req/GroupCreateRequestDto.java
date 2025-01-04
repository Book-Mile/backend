package com.bookmile.backend.domain.group.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class GroupCreateRequestDto {

    @NotBlank(message = "그룹명은 필수입니다.")
    @Schema(description = "그룹명", example = "구루비다")
    private String groupName;

    @NotBlank(message = "그룹 타입은 필수입니다.")
    @Schema(description = "그룹 타입", example = "group")
    private String grouptype; // 개인 또는 단체

    @NotNull(message = "최대 인원수는 필수입니다.(최대 50)")
    @Schema(description = "최대 인원수", example = "10")
    private Integer maxMembers;

    @Schema(description = "비밀번호(선택사항)", example = "1234")
    private String password; // 선택 사항

    @NotBlank(message = "책 ISBN은 필수입니다.")
    @Schema(description = "도서 ISBN13", example = "12345678987654")
    private String isbn13;

    @NotBlank(message = "독서 목표 방식은 필수입니다.")
    @Schema(description = "도서 목표 방식", example = "CHAPTER")
    private String type;

    private String customGoal; // 사용자 정의 목표 (Optional)
}