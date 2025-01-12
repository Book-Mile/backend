package com.bookmile.backend.domain.group.dto.res;

import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.user.entity.User;
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
    private final String goalType;
    private final String goalContent;
    private final String masterNickname;
    private final String masterImage;

    @Builder
    private GroupListResponseDto(Long groupId, String groupName, String groupDescription, String status,
                                 int currentMembers, int maxMembers, String goalType,
                                 String goalContent, String masterNickname, String masterImage) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.status = status;
        this.currentMembers = currentMembers;
        this.maxMembers = maxMembers;
        this.goalType = goalType;
        this.goalContent = goalContent;
        this.masterNickname = masterNickname;
        this.masterImage = masterImage;
    }

    public static GroupListResponseDto toDto(Group group, int currentMembers, User masterUser) {
        return GroupListResponseDto.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .groupDescription(group.getGroupDescription())
                .status(group.getStatus().toString())
                .currentMembers(currentMembers)
                .maxMembers(group.getMaxMembers())
                .goalType(group.getGoalType())
                .goalContent(group.getGoalContent())
                .masterNickname(masterUser.getNickname())
                .masterImage(masterUser.getImage())
                .build();
    }
}