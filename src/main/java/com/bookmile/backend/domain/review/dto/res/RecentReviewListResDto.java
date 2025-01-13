package com.bookmile.backend.domain.review.dto.res;

import com.bookmile.backend.domain.review.entity.Review;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RecentReviewListResDto {
    private Long userId;
    private Long reviewId;
    private String name;
    private Double rating;
    private String text;
    private LocalDateTime createdAt;

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
