package com.bookmile.backend.domain.book.dto;

public record BooklistSearchRequestDto (
        String query,
        String queryType,
        int maxResults
) {
    // 사용자 입력에 따라 기본값 설정
    public BooklistSearchRequestDto(String query) {
        this(query, "Title", 10); // 제목 검색과 최대 10개의 결과
    }
}
