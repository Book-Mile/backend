package com.bookmile.backend.domain.book.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BestSellerResponseDto {
    private String title;
    private String author;
    private String publisher;
    private String cover;
    private int customerReviewRank;
}