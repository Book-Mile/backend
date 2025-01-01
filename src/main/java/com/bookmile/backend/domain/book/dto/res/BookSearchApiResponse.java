package com.bookmile.backend.domain.book.dto.res;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BookSearchApiResponse {
    private List<BookSearchResponseDto> items;
}
