package com.bookmile.backend.domain.book.controller;

import com.bookmile.backend.domain.book.dto.BooklistSearchRequestDto;
import com.bookmile.backend.domain.book.dto.BooklistSearchResponseDto;
import com.bookmile.backend.domain.book.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // 도서 검색 API
    @GetMapping
    public ResponseEntity<List<BooklistSearchResponseDto>> searchBooks(@RequestParam String query) throws Exception {
        // BookSearchRequest 생성
        BooklistSearchRequestDto request = new BooklistSearchRequestDto(query);

        // 서비스 호출 및 결과 반환
        List<BooklistSearchResponseDto> books = bookService.searchBooks(request);
        return ResponseEntity.ok(books);
    }
}
