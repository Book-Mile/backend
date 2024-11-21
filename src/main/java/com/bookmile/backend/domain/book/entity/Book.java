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
import lombok.Getter;

@Entity
@Getter
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private long id;

    @OneToMany(mappedBy = "book")
    private List<Group> group = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<Review> review = new ArrayList<>();

    @Column(nullable = false)
    private String bookName;

    @Column(nullable = false)
    private Integer page;

    @Column(nullable = false)
    private String thumbNail;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}
