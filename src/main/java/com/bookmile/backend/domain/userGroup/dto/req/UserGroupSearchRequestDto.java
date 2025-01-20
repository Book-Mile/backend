package com.bookmile.backend.domain.userGroup.dto.req;


import com.bookmile.backend.domain.group.entity.GroupStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserGroupSearchRequestDto {

    @NotNull(message = "조회하려는 그룹 상태를 입력해주세요")
    @Schema(description = "그룹 상태 (RECRUITING, IN_PROGRESS, COMPLETED)", example = "IN_PROGRESS")
    private GroupStatus status;
}