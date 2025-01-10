package com.bookmile.backend.domain.review.entity;

import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.review.dto.req.ReviewReqDto;
import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.global.config.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JoinColumn(name = "book_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    @Column(nullable = false)
    private Double rating;

    @Column
    private String text;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Builder
    public Review(User user, Book book, Double rating, String text) {
        this.user = user;
        this.book = book;
        this.rating = rating;
        this.text = text;
    }

    public static Review from(User user, Book book, ReviewReqDto reviewReqDto) {
        return Review.builder()
                .user(user)
                .book(book)
                .rating(reviewReqDto.getRating())
                .text(reviewReqDto.getText())
                .build();
    }

    public void update(ReviewReqDto reviewReqDto) {
        this.rating = reviewReqDto.getRating();
        this.text = reviewReqDto.getText();
    }

    public void delete(Review review) {
        this.isDeleted = Boolean.TRUE;
    }
}
