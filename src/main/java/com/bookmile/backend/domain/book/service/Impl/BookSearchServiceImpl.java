package com.bookmile.backend.domain.book.service.impl;

import com.bookmile.backend.domain.book.dto.req.BookSearchRequestDto;
import com.bookmile.backend.domain.book.dto.res.BookSearchResponseDto;
import com.bookmile.backend.domain.book.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookSearchServiceImpl implements BookSearchService {

    private final RestTemplate restTemplate;
    private static final String API_URL = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx";
    private static final String TTB_KEY = "ttbsilkair0011548003"; // 실제 API 키로 교체

    @Override
    public List<BookSearchResponseDto> searchBooks(BookSearchRequestDto requestDto) {
        // 알라딘 API 요청 URL 구성
        String url = String.format("%s?ttbkey=%s&Query=%s&QueryType=%s&MaxResults=%d&Sort=%s&output=xml&Version=20131101",
                API_URL, TTB_KEY, requestDto.getQuery(), requestDto.getQueryType(), requestDto.getMaxResults(), requestDto.getSort());

        // API 호출
        ResponseEntity<BookSearchResponseDto[]> response = restTemplate.getForEntity(url, BookSearchResponseDto[].class);

        // 응답
        BookSearchResponseDto[] books = response.getBody();
        if (books != null) {
            List<BookSearchResponseDto> result = new ArrayList<>();
            for (BookSearchResponseDto book : books) {
                result.add(BookSearchResponseDto.builder()
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .publisher(book.getPublisher())
                        .cover(book.getCover())
                        .link(book.getLink())
                        .isbn13(book.getIsbn13())
                        .build());
            }
            return result;
        }
        return List.of();
    }
}
