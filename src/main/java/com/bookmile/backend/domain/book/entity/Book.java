package com.bookmile.backend.domain.book.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="book_id")
    private long id;

    private String bookName;

    private Integer page;

    private String thumbNail;

    private String publisher;

    private String description;

    private String link;

    private Double rating;


}
