package com.bookmile.backend.domain.group.dto.res;

import com.bookmile.backend.domain.group.entity.GroupStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupStatusUpdateResponseDto {

    private Long groupId;
    private GroupStatus status;

    public GroupStatusUpdateResponseDto(Long groupId, GroupStatus status) {
        this.groupId = groupId;
        this.status = status;
    }
}
