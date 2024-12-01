package com.bookmile.backend.domain.book.service;

import com.bookmile.backend.domain.book.dto.BooklistSearchRequestDto;
import com.bookmile.backend.domain.book.dto.BooklistSearchResponseDto;
import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.book.repository.BookRepository;
import com.bookmile.backend.global.utils.AladinClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final AladinClient aladinClient;
    private final BookRepository bookRepository;

    public BookService(AladinClient aladinClient, BookRepository bookRepository) {
        this.aladinClient = aladinClient;
        this.bookRepository = bookRepository;
    }

    // 도서 검색
    public List<BooklistSearchResponseDto> searchBooks(BooklistSearchRequestDto request) throws Exception {
        // API 호출
        String xmlResponse = aladinClient.searchBooks(request.query(), request.queryType(), request.maxResults());

        // XML 파싱
        List<BooklistSearchResponseDto> books = parseSearchResponse(xmlResponse);

        // DB 저장 (중복 확인 후)
        for (BooklistSearchResponseDto response : books) {
            if (!bookRepository.existsByTitleAndAuthor(response.title(), response.author())) {
                Book book = new Book(
                        response.title(),
                        response.author(),
                        response.publisher(),
                        response.cover(),
                        response.link(),
                        response.description()
                );
                bookRepository.save(book);
            }
        }

        return books;
    }

    private List<BooklistSearchResponseDto> parseSearchResponse(String xml) {
        // XML 데이터를 BookSearchResponse 리스트로 변환하는 로직 추가
        return new ArrayList<>();
    }
}
