package com.bookmile.backend.domain.book.controller;

import com.bookmile.backend.domain.book.dto.BooklistSearchRequestDto;
import com.bookmile.backend.domain.book.dto.BooklistSearchResponseDto;
import com.bookmile.backend.domain.book.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping
    public ResponseEntity<List<BooklistSearchResponseDto>> searchBooks(@RequestParam String query) throws Exception {
        BooklistSearchRequestDto request = new BooklistSearchRequestDto(query);
        List<BooklistSearchResponseDto> books = bookService.searchBooks(request);
        return ResponseEntity.ok(books);
    }
}