package com.bookmile.backend.domain.group.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupJoinRequestDto {

    @Schema(description = "그룹 ID", example = "1")
    @NotNull(message = "그룹 ID는 필수입니다.")
    private Long groupId;

    @Schema(description = "비공개 그룹 비밀번호", example = "1234")
    private String password; // 비공개 그룹일 경우 필요
}
