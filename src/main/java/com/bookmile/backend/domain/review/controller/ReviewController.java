package com.bookmile.backend.domain.review.controller;

import static com.bookmile.backend.global.common.StatusCode.CREATE_REVIEW;
import static com.bookmile.backend.global.common.StatusCode.DELETE_REVIEW;
import static com.bookmile.backend.global.common.StatusCode.UPDATE_REVIEW;
import static com.bookmile.backend.global.common.StatusCode.VIEW_REVIEW;

import com.bookmile.backend.domain.review.dto.req.ReviewReqDto;
import com.bookmile.backend.domain.review.dto.res.ReviewListResDto;
import com.bookmile.backend.domain.review.service.Impl.ReviewServiceImpl;
import com.bookmile.backend.global.common.CommonResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewServiceImpl reviewServiceImpl;

    @GetMapping
    public ResponseEntity<CommonResponse<List<ReviewListResDto>>> viewReviewList(@RequestParam Long bookId) {
        List<ReviewListResDto> reviews = reviewServiceImpl.viewReviewList(bookId);
        return ResponseEntity.status(VIEW_REVIEW.getStatus())
                .body(CommonResponse.from(VIEW_REVIEW.getMessage(), reviews));
    }

    @PostMapping
    public ResponseEntity<CommonResponse<Long>> createReview(@RequestParam Long bookId, @RequestParam Long userId,
                                                             @Valid @RequestBody ReviewReqDto reviewReqDto) {
        Long createReview = reviewServiceImpl.createReview(bookId, userId, reviewReqDto);
        return ResponseEntity.status(CREATE_REVIEW.getStatus())
                .body(CommonResponse.from(CREATE_REVIEW.getMessage(), createReview));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<CommonResponse<Long>> updateReview(@PathVariable Long reviewId,
                                                             @Valid @RequestBody ReviewReqDto reviewReqDto) {
        Long updateReview = reviewServiceImpl.updateReview(reviewId, reviewReqDto);
        return ResponseEntity.status(UPDATE_REVIEW.getStatus())
                .body(CommonResponse.from(UPDATE_REVIEW.getMessage(), updateReview));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<CommonResponse<Long>> deleteReview(@PathVariable Long reviewId) {
        Long deleteReview = reviewServiceImpl.deleteReview(reviewId);
        return ResponseEntity.status(DELETE_REVIEW.getStatus())
                .body(CommonResponse.from(DELETE_REVIEW.getMessage(), deleteReview));
    }
}
