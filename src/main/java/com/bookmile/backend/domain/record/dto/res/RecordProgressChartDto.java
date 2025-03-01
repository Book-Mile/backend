package com.bookmile.backend.domain.record.dto.res;

import com.bookmile.backend.domain.userGroup.entity.UserGroup;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RecordProgressChartDto {
    private final String profile;
    private final String nickname;
    private final String progress;

    @Builder
    public RecordProgressChartDto(String profile, String nickname, String progress) {
        this.profile = profile;
        this.nickname = nickname;
        this.progress = progress;
    }

    public static RecordProgressChartDto from(UserGroup userGroup, double progress) {
        String formatProgress = String.format("%.2f", progress);

        return RecordProgressChartDto.builder()
                .profile(userGroup.getUser().getImage())
                .nickname(userGroup.getUser().getNickname())
                .progress(formatProgress)
                .build();

    }
}
