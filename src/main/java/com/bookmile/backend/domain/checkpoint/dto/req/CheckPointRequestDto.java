package com.bookmile.backend.domain.checkpoint.dto.req;

import com.bookmile.backend.domain.checkpoint.entity.GoalType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckPointRequestDto {

    private GoalType goalType;
    private String freeType;

    public CheckPointRequestDto(GoalType goalType, String freeType) {
        this.goalType = goalType;
        this.freeType = freeType;
    }
}