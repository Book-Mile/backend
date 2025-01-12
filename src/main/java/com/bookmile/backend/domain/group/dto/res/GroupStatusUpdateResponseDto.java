package com.bookmile.backend.domain.group.dto.res;

import com.bookmile.backend.domain.group.entity.GroupStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupStatusUpdateResponseDto {

    private Long groupId;
    private GroupStatus status;

    @Builder
    private GroupStatusUpdateResponseDto(Long groupId, GroupStatus status) {
        this.groupId = groupId;
        this.status = status;
    }

    public static GroupStatusUpdateResponseDto toDto(Long groupId, GroupStatus status) {
        return GroupStatusUpdateResponseDto.builder()
                .groupId(groupId)
                .status(status)
                .build();
    }
}
