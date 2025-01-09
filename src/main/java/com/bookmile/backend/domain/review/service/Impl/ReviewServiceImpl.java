package com.bookmile.backend.domain.review.service.Impl;

import static com.bookmile.backend.global.common.StatusCode.BOOK_NOT_FOUND;
import static com.bookmile.backend.global.common.StatusCode.REVIEW_NOT_FOUND;
import static com.bookmile.backend.global.common.StatusCode.USER_NOT_FOUND;

import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.review.dto.req.ReviewReqDto;
import com.bookmile.backend.domain.review.dto.res.ReviewListResDto;
import com.bookmile.backend.domain.review.entity.Review;
import com.bookmile.backend.domain.review.repository.ReviewRepository;
import com.bookmile.backend.domain.review.service.ReviewBookRepository;
import com.bookmile.backend.domain.review.service.ReviewService;
import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.user.repository.UserRepository;
import com.bookmile.backend.global.exception.CustomException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewBookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public List<ReviewListResDto> viewReviewList(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException(BOOK_NOT_FOUND));

        return reviewRepository.findAllByBookId(book.getId()).stream()
                .map(ReviewListResDto::createReview)
                .collect(Collectors.toList());
    }

    @Override
    public Long createReview(Long bookId, Long userId, ReviewReqDto reviewReqDto) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException(BOOK_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Review review = Review.from(user, book, reviewReqDto);

        reviewRepository.save(review);

        return review.getId();
    }

    @Transactional
    @Override
    public Long updateReview(Long reviewId, ReviewReqDto reviewReqDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));

        review.update(reviewReqDto);

        return review.getId();
    }

    @Transactional
    @Override
    public Long deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));

        review.delete(review);

        return review.getId();
    }
}