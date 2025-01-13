package com.bookmile.backend.domain.review.dto.res;

import com.bookmile.backend.domain.review.entity.Review;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewListResDto {
    private Long userId;
    private Long reviewId;
    private String name;
    private Double rating;
    private String text;
    private LocalDateTime createdAt;

    @Builder
    public ReviewListResDto(Long userId, Long reviewId, String name, Double rating, String text,
                            LocalDateTime createdAt) {
        this.userId = userId;
        this.reviewId = reviewId;
        this.name = name;
        this.rating = rating;
        this.text = text;
        this.createdAt = createdAt;
    }

    public static ReviewListResDto createReview(Review review) {
        return ReviewListResDto.builder()
                .userId(review.getUser().getId())
                .reviewId(review.getId())
                .name(review.getUser().getNickname())
                .rating(review.getRating())
                .text(review.getText())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
