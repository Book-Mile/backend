package com.bookmile.backend.domain.book.service.Impl;

import com.bookmile.backend.domain.book.dto.req.BookDetailRequestDto;
import com.bookmile.backend.domain.book.dto.res.BookDetailApiResponse;
import com.bookmile.backend.domain.book.dto.res.BookDetailResponseDto;
import com.bookmile.backend.domain.book.dto.res.BookListApiResponse;
import com.bookmile.backend.domain.book.service.BookDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookDetailServiceImpl implements BookDetailService {

    private final RestTemplate restTemplate;

    @Value("${aladin.api.url.detail}")
    private String API_URL;

    @Value("${aladin.api.key}")
    private String TTB_KEY;

    @Override
    public List<BookDetailResponseDto> detailBooks(BookDetailRequestDto bookDetailRequestDto) {
        String url = String.format("%s?ttbkey=%s&ItemIdType=ISBN13&ItemId=%s&output=js&Version=20131101",
                API_URL, TTB_KEY, bookDetailRequestDto.getIsbn13());

        ResponseEntity<BookDetailApiResponse> response = restTemplate.getForEntity(url, BookDetailApiResponse.class);

        BookDetailApiResponse apiResponse = response.getBody();

        if (apiResponse != null) {
            List<BookDetailResponseDto> items = apiResponse.getItems();
            return items;
        }
        return List.of();
    }
}
