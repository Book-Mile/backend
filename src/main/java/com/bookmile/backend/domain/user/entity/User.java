package com.bookmile.backend.domain.user.entity;

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

//    public void addUserGroup(UserGroup userGroup) {
//        this.userGroup.add(userGroup);
//        userGroup.addUser(this);
//    }

    @Builder
    public User(String nickname, String email, String password, String image) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.image = image;
    }
}
