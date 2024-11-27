package com.bookmile.backend.domain.review.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.review.entity.Review;
import com.bookmile.backend.domain.user.entity.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void 책_리뷰_책_ID로_조회_올바른_갯수_반환_테스트() {
        //Given
        User user1 = new User("김진용", "kje@naver.com", "1234", "urlurl");
        Book book1 = new Book("김진용의 인생", 456, "image", "김진용", "책설명", "링크url", 5.0);

        testEntityManager.persist(user1);
        testEntityManager.persist(book1);

        Review review1 = new Review(user1, book1, 4.5, "굳이에요 굳");
        reviewRepository.save(review1);

        //When
        List<Review> reviews = reviewRepository.findAllByBookId(book1.getId());

        //Then
        assertEquals(1, reviews.size());
    }


    @Test
    void 책_리뷰_사용자_ID로_조회_올바른_갯수_반환_테스트() {
        //Given
        User user1 = new User("김진용", "kje@naver.com", "1234", "urlurl");
        Book book1 = new Book("김진용의 인생", 456, "image", "김진용", "책설명", "링크url", 5.0);
        Book book2 = new Book("김진용의 인생2", 456, "image", "김진용", "책설명", "링크url", 5.0);

        testEntityManager.persist(user1);
        testEntityManager.persist(book1);
        testEntityManager.persist(book2);

        Review review1 = new Review(user1, book1, 4.5, "굳이에요 굳");
        Review review2 = new Review(user1, book2, 4.5, "이번에도 굳이에요 굳");
        reviewRepository.save(review1);
        reviewRepository.save(review2);

        //When
        List<Review> reviews = reviewRepository.findAllByUserId(user1.getId());

        //Then
        assertEquals(2, reviews.size());
    }
}