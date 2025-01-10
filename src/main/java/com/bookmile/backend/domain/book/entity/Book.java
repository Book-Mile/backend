package com.bookmile.backend.domain.book.entity;

import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.global.config.BaseEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private String description = "제공되는 상세 내용이 없습니다.";

    @Column
    private int totalPage;

    @OneToMany(mappedBy = "book")
    private List<Group> group = new ArrayList<>();


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
}
