package com.bookmile.backend.domain.book.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookDetailResponseDto {

    @Schema(description = "도서 제목", example = "Title Result")
    private String title;

    @Schema(description = "저자", example = "Author Result")
    private String author;

    @Schema(description = "출판사", example = "Publisher Result")
    private String publisher;

    @Schema(description = "도서 커버 이미지 URL", example = "http://example.com/cover.jpg")
    private String cover;

    @Schema(description = "ISBN 번호", example = "12345678987654")
    private String isbn13;

    @Schema(description = "상품 설명", example = "어쩌구")
    private String description;

    //@Schema(description = "상품 페이지", example = "100")
    //private int totalPage;
}
