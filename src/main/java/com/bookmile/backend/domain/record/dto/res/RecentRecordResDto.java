package com.bookmile.backend.domain.record.dto.res;

import com.bookmile.backend.domain.record.entity.Record;
import com.bookmile.backend.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RecentRecordResDto {
    private Long userId;
    private String userImageUrl;
    private String nickName;
    private Integer currentPage;
    private String text;
    private String recordImageUrl;

    @Builder
    public RecentRecordResDto(Long userId, String userImageUrl, String nickName, Integer currentPage, String text,
                              String recordImageUrl) {
        this.userId = userId;
        this.userImageUrl = userImageUrl;
        this.nickName = nickName;
        this.currentPage = currentPage;
        this.text = text;
        this.recordImageUrl = recordImageUrl;
    }

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
