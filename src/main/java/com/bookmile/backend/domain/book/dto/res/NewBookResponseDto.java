package com.bookmile.backend.domain.book.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewBookResponseDto {
    private String title;
    private String author;
    private String publisher;
    private String cover;
    private String isbn13;
    private int customerReviewRank;
}
