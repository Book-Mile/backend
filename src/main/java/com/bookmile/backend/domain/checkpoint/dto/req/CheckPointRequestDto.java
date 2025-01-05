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
    @Schema(description = "PAGE, CHAPTER, NUMBER 중 택 1", example = "CHAPTER")
    private GoalType goalType;

    @NotBlank(message = "자유 스타일 선택도 가능합니다.")
    @Schema(description = "FREE", example = "내맘대로 읽을래용")
    private String freeType;

}