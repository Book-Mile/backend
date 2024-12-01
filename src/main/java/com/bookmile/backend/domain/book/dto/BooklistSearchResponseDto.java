package com.bookmile.backend.domain.book.dto;

public record BooklistSearchResponseDto (
        String title,
        String link,
        String author,
        String publisher,
        String cover,
        String description
) {}