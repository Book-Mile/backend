package com.bookmile.backend.domain.template.dto.res;

import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.template.entity.GoalType;
import com.bookmile.backend.domain.template.entity.Template;
import com.bookmile.backend.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TemplateResponseDto {
    private final Long templateId;
    private final String groupName;
    private final int maxMembers;
    private final String goalType;
    private final String goalContent;
    private final int usageCount;
    private final String masterProfileImage;

    @Builder
    public TemplateResponseDto(Long templateId, String groupName, int maxMembers,
                               GoalType goalType, String goalContent, int usageCount, String masterProfileImage) {
        this.templateId = templateId;
        this.groupName = groupName;
        this.maxMembers = maxMembers;
        this.goalType = String.valueOf(goalType);
        this.goalContent = goalContent;
        this.usageCount = usageCount;
        this.masterProfileImage = masterProfileImage;
    }

    public static TemplateResponseDto toDto(Template template, Group group, User masterUser) {
        return TemplateResponseDto.builder()
                .templateId(template.getId())
                .groupName(group.getGroupName())
                .maxMembers(group.getMaxMembers())
                .goalType(template.getGoalType())
                .goalContent(template.getGoalContent())
                .usageCount(template.getUsageCount())
                .masterProfileImage(masterUser.getImage())
                .build();
    }
}