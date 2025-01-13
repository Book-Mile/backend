package com.bookmile.backend.domain.review.dto.res;

import com.bookmile.backend.domain.review.entity.Review;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RecentReviewListResDto {
    private Long userId;
    private Long reviewId;
    private String name;
    private Double rating;
    private String text;
    private LocalDateTime createdAt;

    @Builder
    public RecentReviewListResDto(Long userId, Long reviewId, String name, Double rating, String text,
                                  LocalDateTime createdAt) {
        this.userId = userId;
        this.reviewId = reviewId;
        this.name = name;
        this.rating = rating;
        this.text = text;
        this.createdAt = createdAt;
    }

    public static RecentReviewListResDto createReview(Review review) {
        return RecentReviewListResDto.builder()
                .userId(review.getUser().getId())
                .reviewId(review.getId())
                .name(review.getUser().getNickname())
                .rating(review.getRating())
                .text(review.getText())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
