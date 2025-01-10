package com.bookmile.backend.domain.book.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BestSellerApiResponse {

    private String title; // 응답 제목

    @JsonProperty("item")
    private List<BestSellerResponseDto> items; // 베스트셀러 도서 목록
}
