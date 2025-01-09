package com.bookmile.backend.domain.record.dto.res;

import com.bookmile.backend.domain.image.entity.Image;
import com.bookmile.backend.domain.record.entity.Record;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RecordListResDto {
    private Long recordId;
    private String text;
    private Integer currentPage;
    private LocalDateTime createdAt;
    private List<Image> imageList;

    public static RecordListResDto createRecord(Record record) {
        return RecordListResDto.builder()
                .recordId(record.getId())
                .text(record.getText())
                .currentPage(record.getCurrentPage())
                .createdAt(record.getCreatedAt())
                .build();
    }
}