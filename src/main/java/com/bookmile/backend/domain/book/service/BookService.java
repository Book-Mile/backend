package com.bookmile.backend.domain.book.service;

import com.bookmile.backend.domain.book.dto.req.BooklistSearchRequestDto;
import com.bookmile.backend.domain.book.dto.res.BooklistSearchResponseDto;
import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.book.repository.BookRepository;
import com.bookmile.backend.global.utils.AladinClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final AladinClient aladinClient;
    private final BookRepository bookRepository;
    private final ObjectMapper objectMapper; // Jackson ObjectMapper

    public BookService(AladinClient aladinClient, BookRepository bookRepository, ObjectMapper objectMapper) {
        this.aladinClient = aladinClient;
        this.bookRepository = bookRepository;
        this.objectMapper = objectMapper;
    }

    public List<BooklistSearchResponseDto> searchBooks(BooklistSearchRequestDto request) throws Exception {
        // 1. 알라딘 API 호출
        String jsonResponse = aladinClient.searchBooks(request.query(), request.queryType(), request.maxResults());

        // 2. JSON 파싱 및 DTO 변환
        List<BooklistSearchResponseDto> bookSearchResponses = parseSearchResponse(jsonResponse);

        // 3. 데이터베이스에 저장
        bookSearchResponses.forEach(response -> {
            Book book = new Book(
                    response.title(),
                    response.link(),
                    response.author(),
                    response.publisher(),
                    response.cover(),
                    response.description()
            );
            bookRepository.save(book);
        });

        return bookSearchResponses;
    }

    private List<BooklistSearchResponseDto> parseSearchResponse(String jsonResponse) throws Exception {
        // JSON 응답에서 item 배열 추출
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray items = jsonObject.getJSONArray("item");

        // JSON 배열을 List<BooklistSearchResponseDto>로 매핑
        return objectMapper.readValue(items.toString(), new TypeReference<>() {
        });
    }
}
