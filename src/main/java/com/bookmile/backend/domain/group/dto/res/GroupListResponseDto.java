package com.bookmile.backend.domain.group.dto.res;

import com.bookmile.backend.domain.group.entity.Group;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GroupListResponseDto {

    private final Long groupId;
    private final String groupName;
    private final String groupDescription;
    private final String status;
    private final int currentMembers;
    private final int maxMembers;

    @Builder
    private GroupListResponseDto(Long groupId, String groupName, String groupDescription, String status,
                                 int currentMembers, int maxMembers) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.status = status;
        this.currentMembers = currentMembers;
        this.maxMembers = maxMembers;
    }

    public static GroupListResponseDto toDto(Group group, int currentMembers) {
        return GroupListResponseDto.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .groupDescription(group.getGroupDescription())
                .status(group.getStatus().toString())
                .currentMembers(currentMembers)
                .maxMembers(group.getMaxMembers())
                .build();
    }
}