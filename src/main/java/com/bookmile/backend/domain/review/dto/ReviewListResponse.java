package com.bookmile.backend.domain.review.dto;

import com.bookmile.backend.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewListResponse {
    private Long userId;
    private String name;
    private double rating;
    private String text;

    public static ReviewListResponse createReview(Review review) {
        return new ReviewListResponse(
                review.getId(),
                review.getUser().getName(),
                review.getRating(),
                review.getText()
        );
    }
}
