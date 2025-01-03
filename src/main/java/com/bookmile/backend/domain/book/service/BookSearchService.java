package com.bookmile.backend.domain.book.service;

import com.bookmile.backend.domain.book.dto.req.BookListRequestDto;
import com.bookmile.backend.domain.book.dto.res.BookListResponseDto;

import java.util.List;

public interface BookSearchService {
    List<BookListResponseDto> searchBooks(BookListRequestDto requestDto);
}
