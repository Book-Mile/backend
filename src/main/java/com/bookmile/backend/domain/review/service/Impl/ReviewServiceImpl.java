package com.bookmile.backend.domain.review.service.Impl;

import static com.bookmile.backend.global.common.StatusCode.BOOK_NOT_FOUND;
import static com.bookmile.backend.global.common.StatusCode.REVIEW_NOT_FOUND;
import static com.bookmile.backend.global.common.StatusCode.USER_NOT_FOUND;

import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.review.dto.req.ReviewReqDto;
import com.bookmile.backend.domain.review.dto.res.RecentReviewListResDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewBookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public Page<ReviewListResDto> viewReviewList(Long bookId,
                                                 Integer pageNumber,
                                                 Integer pageSize) {
        Book book = findBookById(bookId);

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);

        return reviewRepository.findAllByBookId(pageRequest, book.getId())
                .map(ReviewListResDto::createReview);
    }

    @Override
    public List<RecentReviewListResDto> viewRecentReviewList(Long bookId) {
        Book book = findBookById(bookId);

        return reviewRepository.findRecentReviewByBookId(book.getId()).stream()
                .map(RecentReviewListResDto::createReview)
                .collect(Collectors.toList());
    }

    @Override
    public Long createReview(Long bookId, Long userId, ReviewReqDto reviewReqDto) {
        Book book = findBookById(bookId);
        User user = findUserById(userId);

        Review review = Review.from(user, book, reviewReqDto);

        reviewRepository.save(review);
        return review.getId();
    }

    @Transactional
    @Override
    public Long updateReview(Long reviewId, ReviewReqDto reviewReqDto) {
        Review review = findReviewById(reviewId);

        review.update(reviewReqDto);

        return review.getId();
    }

    @Transactional
    @Override
    public Long deleteReview(Long reviewId) {
        Review review = findReviewById(reviewId);

        review.delete(review);

        return review.getId();
    }

    @Override
    public Double totalRate(Long bookId) {
        return reviewRepository.findAverageScore(bookId);
    }

    private Review findReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));
    }

    private Book findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException(BOOK_NOT_FOUND));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}