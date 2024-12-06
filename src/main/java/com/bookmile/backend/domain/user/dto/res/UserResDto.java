package com.bookmile.backend.domain.user.dto.res;

import com.bookmile.backend.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResDto {
    private final Long id;
    private final String email;

    @Builder
    private UserResDto(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public static UserResDto toDto(User user) {
        return UserResDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }
}
