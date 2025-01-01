package com.bookmile.backend.domain.book.controller;

import com.bookmile.backend.domain.book.dto.req.BooklistSearchRequestDto;
import com.bookmile.backend.domain.book.dto.res.BooklistSearchResponseDto;
import com.bookmile.backend.domain.book.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // 도서 검색 API
    @GetMapping("/search")
    public ResponseEntity<List<BooklistSearchResponseDto>> searchBooks(@RequestParam String query) throws Exception {
        BooklistSearchRequestDto request = new BooklistSearchRequestDto(query);

        // 서비스 호출 및 결과 반환
        List<BooklistSearchResponseDto> books = bookService.searchBooks(request);
        return ResponseEntity.ok(books);
    }
}
