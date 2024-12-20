package com.bookmile.backend.domain.review.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewReqDto {
    @NotBlank(message = "평점은 필수 입력 해야합니다.")
    private Double rating;
    private String text;
}
