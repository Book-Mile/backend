package com.bookmile.backend.domain.book.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BookDetailApiResponse {
    private String isbn13;

    @JsonProperty("item") // item 필드 매핑
    private List<BookDetailResponseDto> items;

}
