package com.bookmile.backend.domain.review.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.review.dto.ReviewListResponse;
import com.bookmile.backend.domain.review.entity.Review;
import com.bookmile.backend.domain.review.repository.ReviewRepository;
import com.bookmile.backend.domain.user.entity.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void Book_Id로_리뷰_리스트_가져오기() {
        //Given
        Long bookId = 1L;
        User user1 = new User("김진용", "kje@naver.com", "1234", "urlurl");
        User user2 = new User("김진용", "kje@naver.com", "1234", "urlurl");
        Book book = new Book("김진용의 인생", 456, "image", "김진용", "책설명", "링크url", 5.0);

        Review review1 = new Review(user1, book, 4.5, "굳이에요 굳");
        Review review2 = new Review(user2, book, 4.5, "굳이에요 굳");

        when(reviewRepository.findAllByBookId(bookId)).thenReturn(List.of(review1, review2));

        //When
        List<ReviewListResponse> reviews = reviewService.viewReviewList(bookId);

        //Then
        assertEquals(2, reviews.size());
    }
}