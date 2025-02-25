package com.bookmile.backend.domain.review.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.review.dto.req.ReviewReqDto;
import com.bookmile.backend.domain.review.dto.res.ReviewListResDto;
import com.bookmile.backend.domain.review.entity.Review;
import com.bookmile.backend.domain.review.repository.ReviewRepository;
import com.bookmile.backend.domain.review.service.Impl.ReviewServiceImpl;
import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class ReviewServiceImplTest {

    @Autowired
    private ReviewServiceImpl reviewServiceImpl;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewBookRepository bookRepository;

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
        List<ReviewListResDto> reviews = reviewServiceImpl.viewReviewList(book.getId());

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

        ReviewReqDto reviewReqDto = new ReviewReqDto(4.5, "굳굳");

        // When
        Long reviewId = reviewServiceImpl.createReview(book.getId(), user.getId(), reviewReqDto);

        // Then
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        assertEquals(4.5, review.getRating());
        assertEquals("굳굳", review.getText());
    }

    @Test
    void 리뷰_수정하기() {
        // Given
        User user = new User("김진용", "kje@naver.com", "1234", "urlurl");
        Book book = new Book("김진용의 인생", 456, "image", "김진용", "책설명", "링크url", 5.0);

        userRepository.save(user);
        bookRepository.save(book);

        Review review = new Review(user, book, 4.5, "굳이에요 굳");

        reviewRepository.save(review);

        ReviewReqDto reviewReqDto = new ReviewReqDto(1.0, "다시 생각해보니 별로네요.");

        // When
        Long reviewId = reviewServiceImpl.updateReview(review.getId(), reviewReqDto);

        // Then
        Review updateReview = reviewRepository.findById(reviewId).orElseThrow();
        assertEquals(1.0, updateReview.getRating());
        assertEquals("다시 생각해보니 별로네요.", updateReview.getText());
    }

    @Test
    void 리뷰_삭제하기() {
        // Given
        User user = new User("김진용", "kje@naver.com", "1234", "urlurl");
        Book book = new Book("김진용의 인생", 456, "image", "김진용", "책설명", "링크url", 5.0);

        userRepository.save(user);
        bookRepository.save(book);

        Review review = new Review(user, book, 4.5, "굳이에요 굳");

        reviewRepository.save(review);

        // When
        Long reviewId = reviewServiceImpl.deleteReview(review.getId());

        // Then
        Review deleteReview = reviewRepository.findById(reviewId).orElseThrow();
        assertEquals(Boolean.TRUE, deleteReview.getIsDeleted());
    }
}
