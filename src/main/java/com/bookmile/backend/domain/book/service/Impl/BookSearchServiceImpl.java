package com.bookmile.backend.domain.book.service.Impl;

import com.bookmile.backend.domain.book.dto.req.BookSearchRequestDto;
import com.bookmile.backend.domain.book.dto.res.BookSearchApiResponse;
import com.bookmile.backend.domain.book.dto.res.BookSearchResponseDto;
import com.bookmile.backend.domain.book.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

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
        String url = String.format("%s?ttbkey=%s&Query=%s&QueryType=%s&MaxResults=%d&Sort=%s&output=js&Version=20131101",
                API_URL, TTB_KEY, requestDto.getQuery(), requestDto.getQueryType(), requestDto.getMaxResults(), requestDto.getSort());
        System.out.println("API Request URL: " + url);

        //디버깅 추가
        ResponseEntity<String> rawResponse = restTemplate.getForEntity(url, String.class);
        System.out.println("Raw API Response: " + rawResponse.getBody()); // 디버깅: API 응답 원문 출력

        // JSON 응답을 BookSearchApiResponse로 매핑
        ResponseEntity<BookSearchApiResponse> response = restTemplate.getForEntity(url, BookSearchApiResponse.class);

        // BookSearchApiResponse에서 items 추출
        BookSearchApiResponse apiResponse = response.getBody();

        if (apiResponse != null) {
            List<BookSearchResponseDto> items = apiResponse.getItems();
            System.out.println("Mapped API Response Title: " + apiResponse.getTitle()); // 응답 제목 확인

            System.out.println("API Response Items: " + items); // 디버깅용 로그
            return items;
        }

        System.out.println("API Response is null or empty");
        return List.of();
    }
}
