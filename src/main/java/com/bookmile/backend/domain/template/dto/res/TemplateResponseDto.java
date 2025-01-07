package com.bookmile.backend.domain.template.dto.res;

import com.bookmile.backend.domain.template.entity.GoalType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TemplateResponseDto {

    private final Long id;
    private final GoalType goalType;
    private final int usageCount;

    @Builder
    public TemplateResponseDto(Long id, GoalType goalType, String freeType, int usageCount) {
        this.id = id;
        this.goalType = goalType;
        this.usageCount = usageCount;
    }
}