package com.bookmile.backend.domain.book.service.Impl;

import com.bookmile.backend.domain.book.dto.req.BookDetailRequestDto;
import com.bookmile.backend.domain.book.dto.res.BookDetailResponseDto;
import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.book.repository.BookRepository;
import com.bookmile.backend.domain.book.service.BookDetailService;
import com.bookmile.backend.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookDetailService bookDetailService; // 외부 API 호출

    @Override
    public Book saveBook(String isbn) {
        // 1. 데이터베이스에서 책 정보 조회
        return bookRepository.findById(isbn).orElseGet(() -> {
            // 2. 외부 API 호출
            BookDetailResponseDto bookDto = fetchBookDetail(isbn);

            // 3. 데이터베이스에 저장
            return bookRepository.save(
                    Book.builder()
                            .isbn13(bookDto.getIsbn13())
                            .title(bookDto.getTitle())
                            .author(bookDto.getAuthor())
                            .publisher(bookDto.getPublisher())
                            .totalPage(bookDto.getSubInfo() != null ? bookDto.getSubInfo().getItemPage() : 0)
                            .cover(bookDto.getCover())
                            .description(bookDto.getDescription())
                            .build()
            );
        });
    }

    // 외부 API 호출 로직 분리
    private BookDetailResponseDto fetchBookDetail(String isbn) {
        BookDetailResponseDto bookDetail = bookDetailService.detailBooks(
                new BookDetailRequestDto(isbn)
        ).stream().findFirst().orElseThrow(() ->
                new IllegalArgumentException("책 정보를 가져올 수 없습니다.")
        );
        return bookDetail;
    }
}