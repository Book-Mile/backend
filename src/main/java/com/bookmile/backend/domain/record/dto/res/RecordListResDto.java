package com.bookmile.backend.domain.record.dto.res;

import com.bookmile.backend.domain.record.entity.Record;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecordListResDto {
    private Long recordId;
    private String text;
    private Integer current_page;
    private LocalDateTime createdAt;

    public static RecordListResDto createRecord(Record record) {
        return new RecordListResDto(
                record.getId(),
                record.getText(),
                record.getCurrentPage(),
                record.getCreatedAt()
        );
    }
}