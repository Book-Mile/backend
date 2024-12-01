package com.bookmile.backend.domain.record.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestRecord {
    private String text;
    private Integer currentPage;
}
