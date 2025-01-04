package com.bookmile.backend.domain.book.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookListRequestDto {

    @NotBlank(message = "검색어는 필수 입력 사항입니다.")
    @Schema(description = "도서 검색어", example = "This is Book")
    private String query;

    @Schema(description = "검색 타입 (기본값: Title)", example = "Title")
    private String queryType = "Title";

    @Schema(description = "정렬 방식 (기본값: Accuracy)", example = "Accuracy")
    private String sort = "Accuracy";

    @Schema(description = "페이지 당 결과 수 (기본값: 10)", example = "10")
    private int maxResults = 10;
}