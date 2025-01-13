package com.bookmile.backend.domain.record.dto.res;

import com.bookmile.backend.domain.record.entity.Record;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RecordListResDto {
    private Long recordId;
    private String text;
    private Integer currentPage;
    private LocalDateTime createdAt;
    private List<String> imageUrls;

    @Builder
    public RecordListResDto(Long recordId, String text, Integer currentPage, LocalDateTime createdAt,
                            List<String> imageUrls) {
        this.recordId = recordId;
        this.text = text;
        this.currentPage = currentPage;
        this.createdAt = createdAt;
        this.imageUrls = imageUrls;
    }

    public static RecordListResDto createRecord(Record record, List<String> imageUrls) {
        return RecordListResDto.builder()
                .recordId(record.getId())
                .text(record.getText())
                .currentPage(record.getCurrentPage())
                .createdAt(record.getCreatedAt())
                .imageUrls(imageUrls)
                .build();
    }
}