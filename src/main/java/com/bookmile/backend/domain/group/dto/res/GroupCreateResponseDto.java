package com.bookmile.backend.domain.group.dto.res;

import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.template.entity.Template;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GroupCreateResponseDto {
    private final Long groupId;
    private final String groupName;
    private final String groupDescription;
    private final int maxMembers;
    private final Long templateId;
    private final String goalType;
    private final String goalContent;
    private final String status;

    @Builder
    private GroupCreateResponseDto(Long groupId, String groupName, int maxMembers, String goalType,Long templateId, String goalContent, String status, String groupDescription) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.maxMembers = maxMembers;
        this.templateId = templateId;
        this.goalType = goalType;
        this.goalContent = goalContent;
        this.status = status;
    }

    public static GroupCreateResponseDto toDto(Group group, Template template) {
        return GroupCreateResponseDto.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .groupDescription(group.getGroupDescription())
                .maxMembers(group.getMaxMembers())
                .templateId(template.getId())
                .goalType(group.getGoalType())
                .goalContent(group.getGoalContent())
                .status(group.getStatus().toString())
                .build();
    }

}