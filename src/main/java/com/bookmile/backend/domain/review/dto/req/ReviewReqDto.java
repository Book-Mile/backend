package com.bookmile.backend.domain.review.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewReqDto {
    private Double rating;
    private String text;
}
