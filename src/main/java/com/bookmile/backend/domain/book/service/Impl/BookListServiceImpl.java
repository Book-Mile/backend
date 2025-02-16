package com.bookmile.backend.domain.book.service.Impl;

import com.bookmile.backend.domain.book.dto.req.BookListRequestDto;
import com.bookmile.backend.domain.book.dto.res.*;
import com.bookmile.backend.domain.book.service.BookListService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookListServiceImpl implements BookListService {

    private final RestTemplate restTemplate;

    @Value("${aladin.api.url}")
    private String API_URL;

    @Value("${aladin.api.url.booklist}")
    private String BOOKLIST_API_URL;

    @Value("${aladin.api.key}")
    private String TTB_KEY;

    @Override
    public List<BookListResponseDto> searchBooks(BookListRequestDto requestDto) {
        // 알라딘 API 요청 URL 구성
        String url = String.format("%s?ttbkey=%s&Query=%s&QueryType=%s&MaxResults=%d&Sort=%s&Cover=Big&output=js&Version=20131101",
                API_URL, TTB_KEY, requestDto.getQuery(), requestDto.getQueryType(), requestDto.getMaxResults(), requestDto.getSort());

        // JSON 응답을 BookSearchApiResponse로 매핑
        ResponseEntity<BookListApiResponse> response = restTemplate.getForEntity(url, BookListApiResponse.class);

        // BookSearchApiResponse에서 items 추출
        BookListApiResponse apiResponse = response.getBody();

        if (apiResponse != null) {
            List<BookListResponseDto> items = apiResponse.getItems();
            return items;
        }      return List.of();
    }

    @Override
    public List<BestSellerResponseDto> getBestSellerList() {
        String url = String.format("%s?ttbkey=%s&QueryType=Bestseller&MaxResults=12&start=1&SearchTarget=Book&Cover=Big&output=js&Version=20131101",
                BOOKLIST_API_URL, TTB_KEY);

        ResponseEntity<BestSellerApiResponse> response = restTemplate.getForEntity(url, BestSellerApiResponse.class);

        BestSellerApiResponse apiResponse = response.getBody();

        if (apiResponse != null) {
            List<BestSellerResponseDto> items = apiResponse.getItems();
            return items;
        }
        return List.of();
    }

    @Override
    public List<NewBookResponseDto> getNewBookList() {
        String url = String.format("%s?ttbkey=%s&QueryType=ItemNewSpecial&MaxResults=12&start=1&SearchTarget=Book&Cover=Big&output=js&Version=20131101",
                BOOKLIST_API_URL, TTB_KEY);

        ResponseEntity<NewBookApiResponse> response = restTemplate.getForEntity(url, NewBookApiResponse.class);

        NewBookApiResponse apiResponse = response.getBody();

        if (apiResponse != null) {
            List<NewBookResponseDto> items = apiResponse.getItems();
            return items;
        }
        return List.of();
    }
}
