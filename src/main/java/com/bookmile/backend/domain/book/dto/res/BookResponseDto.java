package com.bookmile.backend.domain.book.dto.res;

import com.bookmile.backend.domain.book.entity.Book;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookResponseDto {

    private String title;
    private String author;
    private String publisher;
    private String cover;
    private String isbn13;
    private String description;
    private int totalPage;

    public BookResponseDto(Book book) {
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.cover = book.getCover();
        this.isbn13 = book.getIsbn13();
        this.description = book.getDescription();
        this.totalPage = book.getTotalPage();
    }
}
