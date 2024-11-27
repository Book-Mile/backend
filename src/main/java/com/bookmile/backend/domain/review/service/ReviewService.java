package com.bookmile.backend.domain.review.service;

import com.bookmile.backend.domain.review.dto.ReviewListResponse;
import com.bookmile.backend.domain.review.repository.ReviewRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
//    private final BookRepository bookRepository;


    public List<ReviewListResponse> viewReviewList(Long bookId) {
        // 책 조회 및 예외처리
//        validateBook(bookId);

        // 리뷰 리스트 조회 및 엔티티 -> DTO 변환
        return reviewRepository.findAllByBookId(bookId).stream()
                .map(ReviewListResponse::createReview)
                .collect(Collectors.toList());
    }

//    private void validateBook(Long bookId){
//        bookRepository.findById(bookId)
//                .orElseThrow(() -> new IllegalArgumentException("없는 소원입니다."));
//    }
}