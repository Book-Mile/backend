package com.bookmile.backend.domain.record.dto;

import com.bookmile.backend.domain.image.entity.Image;
import com.bookmile.backend.domain.record.entity.Record;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecordListResponse {
    private Long recordId;
    private String text;
    private Integer current_page;
    private LocalDateTime createdAt;
    private List<String> images;

    public static RecordListResponse createRecord(Record record) {
        return new RecordListResponse(
                record.getId(),
                record.getText(),
                record.getCurrentPage(),
                record.getCreatedAt(),
                record.getImages().stream()
                        .map(Image::getImageUrl)
                        .collect(Collectors.toList())
        );
    }
}