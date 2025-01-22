package com.bookmile.backend.domain.review.service;

import com.bookmile.backend.domain.review.dto.req.ReviewReqDto;
import com.bookmile.backend.domain.review.dto.res.RecentReviewListResDto;
import com.bookmile.backend.domain.review.dto.res.ReviewListResDto;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ReviewService {
    Page<ReviewListResDto> viewReviewList(Long bookId, Integer pageNumber, Integer pageSize);

    List<RecentReviewListResDto> viewRecentReviewList(Long bookId);

    Long createReview(Long bookId, String userEmail, ReviewReqDto reviewReqDto);

    Long updateReview(Long reviewId, ReviewReqDto reviewReqDto);

    Long deleteReview(Long reviewId);

    Double totalRate(Long bookId);
}

