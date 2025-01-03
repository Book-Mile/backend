package com.bookmile.backend.domain.book.controller;

import com.bookmile.backend.domain.book.dto.req.BookSearchRequestDto;
import com.bookmile.backend.domain.book.dto.res.BookSearchResponseDto;
import com.bookmile.backend.domain.book.service.BookSearchService;
import com.bookmile.backend.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bookmile.backend.global.common.StatusCode.BOOKLIST_SEARCH;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookSearchController {

    private final BookSearchService bookSearchService;

    @Operation(summary = "도서 리스트 검색")
    @PostMapping("/search")
    public ResponseEntity<CommonResponse<List<BookSearchResponseDto>>> searchBooks(
            @Validated @RequestBody BookSearchRequestDto requestDto) {

        List<BookSearchResponseDto> booklist = bookSearchService.searchBooks(requestDto);
        return ResponseEntity.ok(CommonResponse.from(BOOKLIST_SEARCH.getMessage(),booklist));
    }
}