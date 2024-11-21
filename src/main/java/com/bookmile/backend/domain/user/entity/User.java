package com.bookmile.backend.domain.user.entity;

import com.bookmile.backend.domain.review.entity.Review;
import com.bookmile.backend.domain.userGroup.entity.UserGroup;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @OneToMany(mappedBy = "user")
    private List<UserGroup> userGroup = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Review> review = new ArrayList<>();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String image;

    @Column
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private LocalDateTime updatedAt;


    @Column(nullable = false)
    private Boolean isDeleted = false;

    public void addUserGroup(UserGroup userGroup) {
        this.userGroup.add(userGroup);
        userGroup.addUser(this);
    }
}
