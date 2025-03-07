package com.bookmile.backend.domain.book.service;

import com.bookmile.backend.domain.book.dto.req.BookListRequestDto;
import com.bookmile.backend.domain.book.dto.res.BestSellerResponseDto;
import com.bookmile.backend.domain.book.dto.res.BookListResponseDto;
import com.bookmile.backend.domain.book.dto.res.NewBookResponseDto;

import java.util.List;

public interface BookListService {
    List<BookListResponseDto> searchBooks(BookListRequestDto requestDto);
    List<BestSellerResponseDto> getBestSellerList();
    List<NewBookResponseDto> getNewBookList();
}
