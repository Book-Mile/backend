package com.bookmile.backend.domain.user.dto.res;

import com.bookmile.backend.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDetailResDto {
    private final String email;
    private final String nickName;
    private final String image;

    @Builder
    private UserDetailResDto(String email, String nickName, String image) {
        this.email = email;
        this.nickName = nickName;
        this.image = image;
    }

    public static UserDetailResDto toDto(User user) {
        return UserDetailResDto.builder()
                .email(user.getEmail())
                .nickName(user.getNickname())
                .image(user.getImage())
                .build();
    }
}
