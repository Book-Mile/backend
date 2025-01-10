package com.bookmile.backend.domain.book.controller;

import com.bookmile.backend.domain.book.dto.req.BookDetailRequestDto;
import com.bookmile.backend.domain.book.dto.req.BookListRequestDto;
import com.bookmile.backend.domain.book.dto.res.*;
import com.bookmile.backend.domain.book.service.BookDetailService;
import com.bookmile.backend.domain.book.service.BookListService;
import com.bookmile.backend.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bookmile.backend.global.common.StatusCode.*;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookListService bookListService;
    private final BookDetailService bookDetailService;

    @Operation(summary = "도서 리스트 검색", description = "도서 리스트를 검색합니다. 입력한 검색어와 가장 유사한 도서 10개의 정보를 반환합니다. <br>"
    + "현재 책 제목을 통한 검색만 가능하며 정확한 제목을 입력 할 필요는 없습니다. swagger 사용시 title 만 수정하여 검색합니다.")
    @PostMapping("/search")
    public ResponseEntity<CommonResponse<List<BookListResponseDto>>> searchBooks(
            @Validated @RequestBody BookListRequestDto requestDto) {
        List<BookListResponseDto> booklist = bookListService.searchBooks(requestDto);
        return ResponseEntity.ok(CommonResponse.from(BOOKLIST_SEARCH.getMessage(),booklist));
    }

    @Operation(summary = "도서 상세 검색", description = "선택한 책 하나의 상세 정보를 검색합니다. 도서의 ISBN13 번호를 기반으로 검색합니다.")
    @PostMapping("/detail")
    public ResponseEntity<CommonResponse<List<BookDetailResponseDto>>> detailBooks(
            @Validated @RequestBody BookDetailRequestDto requestDto) {
        List<BookDetailResponseDto> bookdetail = bookDetailService.detailBooks(requestDto);
        return ResponseEntity.ok(CommonResponse.from(BOOKDETAIL_SEARCH.getMessage(),bookdetail));
    }

    @Operation(summary = "베스트셀러 검색", description = "베스트셀러 10권을 조회합니다.")
    @GetMapping("/best-sellers")
    public ResponseEntity<CommonResponse<List<BestSellerResponseDto>>> getBestSellers() {
        List<BestSellerResponseDto> bestSellerList = bookListService.getBestSellerList();
        return ResponseEntity.ok(CommonResponse.from(BESTSELLER_SEARCH.getMessage(), bestSellerList));
    }

    @Operation(summary = "추천 신간 도서 검색", description = "알라딘 추천 신간도서 10권을 조회합니다.")
    @GetMapping("/new-books")
    public ResponseEntity<CommonResponse<List<NewBookResponseDto>>> getNewBooks() {
        List<NewBookResponseDto> newBookList = bookListService.getNewBookList();
        return ResponseEntity.ok(CommonResponse.from(NEWBOOK_SEARCH.getMessage(), newBookList));
    }
}