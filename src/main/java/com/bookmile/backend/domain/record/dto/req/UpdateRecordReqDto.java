package com.bookmile.backend.domain.record.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UpdateRecordReqDto {
    private String text;

    @NotNull(message = "페이지 기록은 필수입니다.")
    private Integer currentPage;
}
