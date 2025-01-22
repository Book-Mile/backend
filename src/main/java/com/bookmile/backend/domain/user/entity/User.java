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


    @Builder
    public User(String nickname, String email, String password, String image, Boolean isDeleted, UserRole role) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.image = image;
        this.isDeleted = isDeleted;
        this.role = role;
    }

    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateImage(String image) {
        this.image = image;
    }

    public void updateIsDeleted() {
        this.isDeleted = !this.isDeleted;
    }

    public void updateUser(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

    public User(Long id) {
        this.id = id;
    }
}

