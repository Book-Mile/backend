package com.bookmile.backend.domain.checkpoint.dto.res;

import com.bookmile.backend.domain.checkpoint.entity.GoalType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class CheckPointResponseDto {

    private final Long id;
    private final GoalType goalType;
    private final String freeType;
    private final int usageCount;

    @Builder
    public CheckPointResponseDto(Long id, GoalType goalType, String freeType, int usageCount) {
        this.id = id;
        this.goalType = goalType;
        this.freeType = freeType;
        this.usageCount = usageCount;
    }
}