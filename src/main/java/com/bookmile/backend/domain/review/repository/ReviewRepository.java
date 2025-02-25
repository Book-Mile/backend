package com.bookmile.backend.domain.review.repository;

import com.bookmile.backend.domain.review.entity.Review;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByBookId(Pageable pageable, Long bookId);

    List<Review> findAllByUserId(Long userId);

    @Query(value = "SELECT ROUND(AVG(review.rating), 2) FROM review  WHERE book_id = :bookId", nativeQuery = true)
    Double findAverageScore(Long bookId);

    @Query(value = "SELECT * FROM review WHERE book_id = :bookId ORDER BY created_at DESC LIMIT 2", nativeQuery = true)
    List<Review> findRecentReviewByBookId(Long bookId);
}
