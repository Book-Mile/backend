package com.bookmile.backend.domain.book.service;

import com.bookmile.backend.domain.book.dto.req.BookSearchRequestDto;
import com.bookmile.backend.domain.book.dto.res.BookSearchResponseDto;

import java.util.List;

public interface BookSearchService {
    List<BookSearchResponseDto> searchBooks(BookSearchRequestDto requestDto);
}
