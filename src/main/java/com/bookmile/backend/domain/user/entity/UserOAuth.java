package com.bookmile.backend.domain.user.entity;

import com.bookmile.backend.global.config.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserOAuth extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oauth_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // OAuth2.0 제공자
    @Column(nullable = false)
    private String provider;

    // OAuth 로그인 유저의 고유 ID
    @Column(name= "provider_id", nullable = false, unique = true)
    private String providerId;

    @Builder
    public UserOAuth(User user, String provider, String providerId) {
        this.user = user;
        this.provider = provider;
        this.providerId = providerId;
    }
}
