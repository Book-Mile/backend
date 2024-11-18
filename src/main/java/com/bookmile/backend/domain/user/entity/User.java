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

    private String name;

    private String email;

    private String password;

    private String image;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean isDeleted;

    public void addUserGroup(UserGroup userGroup) {
        this.userGroup.add(userGroup);
        userGroup.addUser(this);
    }
}
