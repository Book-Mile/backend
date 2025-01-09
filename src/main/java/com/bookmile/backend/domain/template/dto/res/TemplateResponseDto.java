package com.bookmile.backend.domain.template.dto.res;

import com.bookmile.backend.domain.template.entity.GoalType;
import com.bookmile.backend.domain.template.entity.Template;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TemplateResponseDto {

    private final Long templateId;

    private final String goalType;

    private final String goalContent;

    private final int usageCount;

    @Builder
    public TemplateResponseDto(Long templateId, GoalType goalType, String goalContent, int usageCount) {
        this.templateId = templateId;
        this.goalType = String.valueOf(goalType);
        this.goalContent = goalContent;
        this.usageCount = usageCount;
    }

    public static TemplateResponseDto from(Template template) {
        return TemplateResponseDto.builder()
                .templateId(template.getId())
                .goalType(template.getGoalType())
                .goalContent(template.getGoalContent())
                .usageCount(template.getUsageCount())
                .build();
    }
}
