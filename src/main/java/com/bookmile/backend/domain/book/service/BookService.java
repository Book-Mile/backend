package com.bookmile.backend.domain.book.service;

import com.bookmile.backend.domain.book.entity.Book;

public interface BookService {
    Book saveBook(String isbn);
}
