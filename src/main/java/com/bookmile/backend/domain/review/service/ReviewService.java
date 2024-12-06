package com.bookmile.backend.domain.review.service;

import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.review.dto.req.ReviewReqDto;
import com.bookmile.backend.domain.review.dto.res.ReviewListResDto;
import com.bookmile.backend.domain.review.entity.Review;
import com.bookmile.backend.domain.review.repository.ReviewRepository;
import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public List<ReviewListResDto> viewReviewList(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("없는 책입니다."));

        return reviewRepository.findAllByBookId(bookId).stream()
                .map(ReviewListResDto::createReview)
                .collect(Collectors.toList());
    }

    public Long createReview(Long bookId, Long userId, ReviewReqDto reviewReqDto) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("없는 책입니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("없는 사용자입니다."));

        Review review = Review.from(user, book, reviewReqDto);

        reviewRepository.save(review);

        return review.getId();
    }

    public Long updateReview(Long reviewId, ReviewReqDto reviewReqDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("없는 리뷰입니다."));

        review.update(reviewReqDto);
        reviewRepository.save(review);

        return review.getId();
    }

    public Long deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("없는 리뷰입니다."));

        review.delete(review);

        reviewRepository.save(review);

        return review.getId();
    }
}