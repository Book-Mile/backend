package com.bookmile.backend.domain.user.dto.res;


import com.bookmile.backend.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoDto {
    private final Long userId;
    private final String nickName;
    private final String email;
    private final String image;

    @Builder
    public UserInfoDto(Long userId, String nickName, String email, String image) {
        this.userId = userId;
        this.nickName = nickName;
        this.email = email;
        this.image = image;
    }

    public static UserInfoDto toDto(User user) {
        return UserInfoDto.builder()
                .userId(user.getId())
                .nickName(user.getNickname())
                .email(user.getEmail())
                .image(user.getImage())
                .build();
    }
}
