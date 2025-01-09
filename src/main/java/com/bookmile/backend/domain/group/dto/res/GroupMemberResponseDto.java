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

    @Builder
    public GroupMemberResponseDto(Long userId, String nickname, Role role) {
        this.userId = userId;
        this.nickname = nickname;
        this.role = role;
    }
}