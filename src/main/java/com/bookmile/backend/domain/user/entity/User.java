package com.bookmile.backend.domain.user.entity;

import com.bookmile.backend.global.common.UserRole;
import com.bookmile.backend.global.config.BaseEntity;
import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = true, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    private String password;

    @Column
    private String image;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    // OAuth2.0 제공자
    private String provider;

    // OAuth 로그인 유저의 고유 ID
    private String providerId;

    @Builder
    public User(String nickname, String email, String password, String image, Boolean isDeleted, UserRole role, String provider, String providerId) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.image = image;
        this.isDeleted = isDeleted;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateImage(String image) {
        this.image = image;
    }

    public void updateIsDeleted() {
        this.isDeleted = true;
    }

    public void updateUser(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }
}

