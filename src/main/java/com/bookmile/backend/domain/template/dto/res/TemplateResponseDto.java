package com.bookmile.backend.domain.template.dto.res;

import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.template.entity.GoalType;
import com.bookmile.backend.domain.template.entity.Template;
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

    @Builder
    public TemplateResponseDto(Long templateId, String groupName, int maxMembers,
                               GoalType goalType, String goalContent, int usageCount) {
        this.templateId = templateId;
        this.groupName = groupName;
        this.maxMembers = maxMembers;
        this.goalType = String.valueOf(goalType);
        this.goalContent = goalContent;
        this.usageCount = usageCount;
    }

    public static TemplateResponseDto from(Template template, Group group) {
        return TemplateResponseDto.builder()
                .templateId(template.getId())
                .groupName(group.getGroupName())
                .maxMembers(group.getMaxMembers())
                .goalType(template.getGoalType())
                .goalContent(template.getGoalContent())
                .usageCount(template.getUsageCount())
                .build();
    }
}
