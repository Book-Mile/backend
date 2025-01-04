package com.bookmile.backend.domain.book.entity;

import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.review.entity.Review;
import com.bookmile.backend.global.config.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private long id;

    @Column(length = 13)
    private String isbn13;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private String cover;

    @Column(nullable = false)
    private String description;

    @Column
    private int totalPage;

    @OneToMany(mappedBy = "book")
    private List<Group> group = new ArrayList<>();

    //@OneToMany(mappedBy = "book")
    //private List<Review> review = new ArrayList<>();

    //@Column
    //private Integer bestSellerRank = null; //상품 조회하면 업데이트

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Builder
    public Book(String isbn13, String title, String author, String publisher, String cover, String description, int totalPage) {
        this.isbn13 = isbn13;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.cover = cover;
        this.description = description;
        this.totalPage = totalPage;
    }

    protected Book() {

    }
}
