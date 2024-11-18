package com.bookmile.backend.domain.book.entity;

import com.bookmile.backend.domain.group.entity.Group;
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
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private long id;

    @OneToMany(mappedBy = "book")
    private List<Group> group = new ArrayList<>();

    private String bookName;

    private Integer page;

    private String thumbNail;

    private String publisher;

    private String description;

    private String link;

    private Double rating;
}
