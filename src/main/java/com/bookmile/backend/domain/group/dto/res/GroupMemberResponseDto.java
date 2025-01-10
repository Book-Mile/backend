package com.bookmile.backend.domain.group.dto.res;

import com.bookmile.backend.domain.userGroup.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupMemberResponseDto {
    private Long userId;
    private String nickname;
    private Role role;
    private String image;

    @Builder
    public GroupMemberResponseDto(Long userId, String nickname, Role role, String image) {
        this.userId = userId;
        this.nickname = nickname;
        this.role = role;
        this.image = image;
    }
}