package com.bookmile.backend.domain.user.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoReqDto {

    private String nickname;

    private String email;
}
