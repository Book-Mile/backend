package com.bookmile.backend.domain.group.dto.res;

import com.bookmile.backend.domain.group.entity.Group;
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

    public static GroupJoinResponseDto toDto(Group group) {
        return GroupJoinResponseDto.builder()
                .groupId(group.getId())
                .password(group.getPassword())
                .build();
    }
}
