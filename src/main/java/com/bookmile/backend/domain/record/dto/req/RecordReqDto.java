package com.bookmile.backend.domain.record.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecordReqDto {
    private String text;

    @NotBlank(message = "페이지 기록은 필수입니다.")
    private Integer currentPage;
}
