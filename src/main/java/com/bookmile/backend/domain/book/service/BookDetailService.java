package com.bookmile.backend.domain.book.service;

import com.bookmile.backend.domain.book.dto.req.BookDetailRequestDto;
import com.bookmile.backend.domain.book.dto.res.BookDetailResponseDto;

import java.util.List;

public interface BookDetailService {
    List<BookDetailResponseDto> searchBookDetails(BookDetailRequestDto bookDetailRequestDto);
}
