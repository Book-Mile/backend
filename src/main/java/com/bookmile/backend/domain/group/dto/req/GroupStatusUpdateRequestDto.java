package com.bookmile.backend.domain.group.dto.req;

import com.bookmile.backend.domain.group.entity.GroupStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupStatusUpdateRequestDto {

    @Schema(description = "그룹 상태 (RECRUITING, IN_PROGRESS, COMPLETED)", example = "IN_PROGRESS")
    @NotNull(message = "변경할 상태는 필수입니다.")
    private GroupStatus status;
}
