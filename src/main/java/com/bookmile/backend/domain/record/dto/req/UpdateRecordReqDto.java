package com.bookmile.backend.domain.record.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UpdateRecordReqDto {
    private String text;
    private Integer currentPage;
}
