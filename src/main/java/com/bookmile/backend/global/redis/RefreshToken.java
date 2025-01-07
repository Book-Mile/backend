package com.bookmile.backend.global.redis;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value="refreshToken", timeToLive = 86400)
public class RefreshToken{

    @Id
    private String id;

    @Indexed
    private Long userId;

    private String refreshToken;

    @Builder
    public RefreshToken(String id,Long userId, String refreshToken) {
        this.id = "refreshToken" + userId;
        this.userId = userId;
        this.refreshToken = refreshToken;
    }

}
