package com.bookmile.backend.domain.book.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NewBookApiResponse {

    private String title; // 응답 제목

    @JsonProperty("item")
    private List<BestSellerItem> items; // 베스트셀러 도서 목록

    @Getter
    @NoArgsConstructor
    public static class BestSellerItem {
        private String title;       // 도서 제목
        private String author;      // 저자
        private String publisher;   // 출판사
        private String cover;       // 커버 이미지
        private int customerReviewRank; // 평점
    }
}
