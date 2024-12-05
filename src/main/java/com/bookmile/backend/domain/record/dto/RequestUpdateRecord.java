package com.bookmile.backend.domain.record.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class RequestUpdateRecord {
    private String text;
    private Integer currentPage;
}
