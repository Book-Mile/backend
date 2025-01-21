package com.bookmile.backend.domain.review.controller;

import static com.bookmile.backend.global.common.StatusCode.CREATE_REVIEW;
import static com.bookmile.backend.global.common.StatusCode.DELETE_REVIEW;
import static com.bookmile.backend.global.common.StatusCode.UPDATE_REVIEW;
import static com.bookmile.backend.global.common.StatusCode.VIEW_BOOK_REVIEW_RATE;
import static com.bookmile.backend.global.common.StatusCode.VIEW_REVIEW;

import com.bookmile.backend.domain.review.dto.req.ReviewReqDto;
import com.bookmile.backend.domain.review.dto.res.RecentReviewListResDto;
import com.bookmile.backend.domain.review.dto.res.ReviewListResDto;
import com.bookmile.backend.domain.review.service.ReviewService;
import com.bookmile.backend.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @Operation(summary = "리뷰 리스트 조회", description = "해당 책의 모든 사용자 리뷰 목록을 페이지별로 조회합니다."
            + "pageNumber은 1부터 사용해주시면 될 것 같습니다!")
    @GetMapping
    public ResponseEntity<CommonResponse<Page<ReviewListResDto>>> viewReviewList(@RequestParam Long bookId,
                                                                                 @RequestParam Integer pageNumber,
                                                                                 @RequestParam Integer pageSize) {
        Page<ReviewListResDto> reviews = reviewService.viewReviewList(bookId, pageNumber, pageSize);
        return ResponseEntity.status(VIEW_REVIEW.getStatus())
                .body(CommonResponse.from(VIEW_REVIEW.getMessage(), reviews));
    }

    @Operation(summary = "최신 리뷰 2개 조회", description = "해당 책의 가장 최근 리뷰 2개를 반환합니다.")
    @GetMapping("/recent-reviews")
    public ResponseEntity<CommonResponse<List<RecentReviewListResDto>>> recentReviewList(@RequestParam Long bookId) {
        List<RecentReviewListResDto> reviews = reviewService.viewRecentReviewList(bookId);
        return ResponseEntity.status(VIEW_REVIEW.getStatus())
                .body(CommonResponse.from(VIEW_REVIEW.getMessage(), reviews));
    }

    @Operation(summary = "리뷰 작성", description = "해당 책의 리뷰를 작성합니다.")
    @PostMapping
    public ResponseEntity<CommonResponse<Long>> createReview(@RequestParam Long bookId, @RequestParam Long userId,
                                                             @Valid @RequestBody ReviewReqDto reviewReqDto) {
        Long createReview = reviewService.createReview(bookId, userId, reviewReqDto);
        return ResponseEntity.status(CREATE_REVIEW.getStatus())
                .body(CommonResponse.from(CREATE_REVIEW.getMessage(), createReview));
    }

    @Operation(summary = "리뷰 수정", description = "리뷰를 수정합니다.")
    @PutMapping("/{reviewId}")
    public ResponseEntity<CommonResponse<Long>> updateReview(@PathVariable Long reviewId,
                                                             @Valid @RequestBody ReviewReqDto reviewReqDto) {
        Long updateReview = reviewService.updateReview(reviewId, reviewReqDto);
        return ResponseEntity.status(UPDATE_REVIEW.getStatus())
                .body(CommonResponse.from(UPDATE_REVIEW.getMessage(), updateReview));
    }

    @Operation(summary = "리뷰 삭제", description = "해당 리뷰를 삭제합니다.")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<CommonResponse<Long>> deleteReview(@PathVariable Long reviewId) {
        Long deleteReview = reviewService.deleteReview(reviewId);
        return ResponseEntity.status(DELETE_REVIEW.getStatus())
                .body(CommonResponse.from(DELETE_REVIEW.getMessage(), deleteReview));
    }

    @Operation(summary = "해당 책의 리뷰 전체 평점 반환", description = "책의 리뷰 전체 평점을 조회합니다.")
    @GetMapping("/{bookId}/total-rate")
    public ResponseEntity<CommonResponse<Double>> totalRateView(@PathVariable Long bookId) {
        Double bookTotalRate = reviewService.totalRate(bookId);
        return ResponseEntity.status(VIEW_BOOK_REVIEW_RATE.getStatus())
                .body(CommonResponse.from(VIEW_BOOK_REVIEW_RATE.getMessage(), bookTotalRate));
    }
}
