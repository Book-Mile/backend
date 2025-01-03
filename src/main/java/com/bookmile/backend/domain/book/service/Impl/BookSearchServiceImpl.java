package com.bookmile.backend.domain.book.service.Impl;

import com.bookmile.backend.domain.book.dto.req.BookListRequestDto;
import com.bookmile.backend.domain.book.dto.res.BookSearchApiResponse;
import com.bookmile.backend.domain.book.dto.res.BookListResponseDto;
import com.bookmile.backend.domain.book.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookSearchServiceImpl implements BookSearchService {

    private final RestTemplate restTemplate;

    @Value("${aladin.api.url}")
    private String API_URL;

    @Value("${aladin.api.key}")
    private String TTB_KEY;

    @Override
    public List<BookListResponseDto> searchBooks(BookListRequestDto requestDto) {
        // 알라딘 API 요청 URL 구성
        String url = String.format("%s?ttbkey=%s&Query=%s&QueryType=%s&MaxResults=%d&Sort=%s&output=js&Version=20131101",
                API_URL, TTB_KEY, requestDto.getQuery(), requestDto.getQueryType(), requestDto.getMaxResults(), requestDto.getSort());

        // JSON 응답을 BookSearchApiResponse로 매핑
        ResponseEntity<BookSearchApiResponse> response = restTemplate.getForEntity(url, BookSearchApiResponse.class);

        // BookSearchApiResponse에서 items 추출
        BookSearchApiResponse apiResponse = response.getBody();

        if (apiResponse != null) {
            List<BookListResponseDto> items = apiResponse.getItems();
            return items;
        }

        return List.of();
    }
}
