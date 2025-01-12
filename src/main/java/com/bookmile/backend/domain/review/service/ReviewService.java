package com.bookmile.backend.domain.review.service;

import com.bookmile.backend.domain.review.dto.req.ReviewReqDto;
import com.bookmile.backend.domain.review.dto.res.RecentReviewListResDto;
import com.bookmile.backend.domain.review.dto.res.ReviewListResDto;
import java.util.List;

public interface ReviewService {
    List<ReviewListResDto> viewReviewList(Long bookId);

    List<RecentReviewListResDto> viewRecentReviewList(Long bookId);

    Long createReview(Long bookId, Long userId, ReviewReqDto reviewReqDto);

    Long updateReview(Long reviewId, ReviewReqDto reviewReqDto);

    Long deleteReview(Long reviewId);

    Double totalRate(Long bookId);
}

