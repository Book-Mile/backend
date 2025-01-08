package com.bookmile.backend.domain.group.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class GroupJoinResponseDto {
    private final long groupId;
    private final String password;

    @Builder
    public GroupJoinResponseDto(long groupId, String password) {
        this.groupId = groupId;
        this.password = password;
    }
}
