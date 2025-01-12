package com.bookmile.backend.domain.review.dto.res;

import com.bookmile.backend.domain.review.entity.Review;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewListResDto {
    private Long userId;
    private Long reviewId;
    private String name;
    private Double rating;
    private String text;
    private LocalDateTime createdAt;

    public static ReviewListResDto createReview(Review review) {
        return new ReviewListResDto(
                review.getId(),
                review.getUser().getId(),
                review.getUser().getNickname(),
                review.getRating(),
                review.getText(),
                review.getCreatedAt()
        );
    }
}
