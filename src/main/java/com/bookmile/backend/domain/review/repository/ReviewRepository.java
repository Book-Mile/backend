package com.bookmile.backend.domain.review.repository;

import com.bookmile.backend.domain.review.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByBookId(Long bookId);

    List<Review> findAllByUserId(Long userId);

    @Query(value = "SELECT ROUND(AVG(review.rating), 2) FROM review  WHERE book_id = :bookId", nativeQuery = true)
    Double findAverageScore(Long bookId);
}
