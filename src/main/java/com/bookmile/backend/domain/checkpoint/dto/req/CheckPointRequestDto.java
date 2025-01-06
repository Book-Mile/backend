package com.bookmile.backend.domain.checkpoint.dto.req;

import com.bookmile.backend.domain.checkpoint.entity.GoalType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckPointRequestDto {

    @NotBlank(message = "목표 타입을 선택해주세요.")
    @Schema(description = "PAGE, CHAPTER, NUMBER, CUSTOM", example = "CHAPTER")
    private GoalType goalType;

}