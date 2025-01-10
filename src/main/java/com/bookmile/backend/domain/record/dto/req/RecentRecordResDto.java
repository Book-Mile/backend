package com.bookmile.backend.domain.record.dto.req;

import com.bookmile.backend.domain.record.entity.Record;
import com.bookmile.backend.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RecentRecordResDto {
    private Long userId;
    private String userImageUrl;
    private String nickName;
    private Integer currentPage;
    private String text;
    private String recordImageUrl;

    public static RecentRecordResDto createRecentRecord(User user, Record record, String recordImage) {
        return RecentRecordResDto.builder()
                .userId(user.getId())
                .userImageUrl(user.getImage())
                .nickName(user.getNickname())
                .currentPage(record.getCurrentPage())
                .text(record.getText())
                .recordImageUrl(recordImage)
                .build();
    }
}
