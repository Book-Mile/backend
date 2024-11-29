package com.bookmile.backend.domain.review.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.review.dto.ReviewListResponse;
import com.bookmile.backend.domain.review.dto.ReviewRequest;
import com.bookmile.backend.domain.review.entity.Review;
import com.bookmile.backend.domain.review.repository.ReviewRepository;
import com.bookmile.backend.domain.user.entity.User;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void Book_Id로_리뷰_리스트_가져오기() {
        // Given
        User user1 = new User("김진용", "kje@naver.com", "1234", "urlurl");
        User user2 = new User("진용짱", "jin@naver.com", "5678", "urlurl2");
        Book book = new Book("김진용의 인생", 456, "image", "김진용", "책설명", "링크url", 5.0);

        userRepository.save(user1);
        userRepository.save(user2);
        bookRepository.save(book);

        Review review1 = new Review(user1, book, 4.5, "굳이에요 굳");
        Review review2 = new Review(user2, book, 4.0, "좋아요!");

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        // When
        List<ReviewListResponse> reviews = reviewService.viewReviewList(book.getId());

        // Then
        assertEquals(2, reviews.size());
        assertEquals("굳이에요 굳", reviews.get(0).getText());
        assertEquals("좋아요!", reviews.get(1).getText());
    }

    @Test
    void 리뷰_생성하기() {
        // Given
        User user = new User("김진용", "kje@naver.com", "1234", "urlurl");
        Book book = new Book("김진용의 인생", 456, "image", "김진용", "책설명", "링크url", 5.0);

        userRepository.save(user);
        bookRepository.save(book);

        ReviewRequest reviewRequest = new ReviewRequest(4.5, "굳굳");

        // When
        Long reviewId = reviewService.createReview(book.getId(), user.getId(), reviewRequest);

        // Then
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        assertEquals(4.5, review.getRating());
        assertEquals("굳굳", review.getText());
    }
}
