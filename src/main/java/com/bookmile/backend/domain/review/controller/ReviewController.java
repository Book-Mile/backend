package com.bookmile.backend.domain.review.controller;

import com.bookmile.backend.domain.review.dto.ReviewListResponse;
import com.bookmile.backend.domain.review.dto.ReviewRequest;
import com.bookmile.backend.domain.review.service.ReviewService;
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
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewListResponse>> viewReviewList(@RequestParam Long bookId) {
        List<ReviewListResponse> reviews = reviewService.viewReviewList(bookId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<Long> createReview(@RequestParam Long bookId, @RequestParam Long userId,
                                             @RequestBody ReviewRequest reviewRequest) {
        Long createReview = reviewService.createReview(bookId, userId, reviewRequest);
        return ResponseEntity.ok(createReview);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Long> updateReview(@PathVariable Long reviewId, @RequestBody ReviewRequest reviewRequest) {
        Long updateReview = reviewService.updateReview(reviewId, reviewRequest);
        return ResponseEntity.ok(updateReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Long> deleteReview(@PathVariable Long reviewId) {
        Long deleteReview = reviewService.deleteReview(reviewId);
        return ResponseEntity.ok(deleteReview);
    }
}
