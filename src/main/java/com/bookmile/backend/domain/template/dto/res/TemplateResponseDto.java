package com.bookmile.backend.domain.template.dto.res;

import com.bookmile.backend.domain.template.entity.GoalType;
import com.bookmile.backend.domain.template.entity.Template;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

@Getter
public class TemplateResponseDto {

    private final Long templateId;

    private final String goalType;

    private final String customGoal;

    private final int usageCount;

    @Builder
    public TemplateResponseDto(Long templateId, GoalType goalType, String customGoal, int usageCount) {
        this.templateId = templateId;
        this.goalType = String.valueOf(goalType);
        this.customGoal = customGoal;
        this.usageCount = usageCount;
    }

    public static TemplateResponseDto from(Template template) {
        return TemplateResponseDto.builder()
                .templateId(template.getId())
                .goalType(template.getGoalType())
                .customGoal(template.getCustomGoal())
                .usageCount(template.getUsageCount())
                .build();
    }
}
