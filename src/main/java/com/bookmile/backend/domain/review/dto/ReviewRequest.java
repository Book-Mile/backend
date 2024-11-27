package com.bookmile.backend.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewRequest {
    private Double rating;
    private String text;
}
