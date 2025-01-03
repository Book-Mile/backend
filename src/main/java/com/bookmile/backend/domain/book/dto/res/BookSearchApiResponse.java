package com.bookmile.backend.domain.book.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BookSearchApiResponse {
    private String title;

    @JsonProperty("item") // item 필드 매핑
    private List<BookListResponseDto> items;
}
