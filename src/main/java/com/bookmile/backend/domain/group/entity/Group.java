package com.bookmile.backend.domain.group.entity;

import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.checkpoint.entity.CheckPoint;
import com.bookmile.backend.domain.userGroup.entity.UserGroup;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @OneToMany(mappedBy = "group")
    private List<UserGroup> userGroup = new ArrayList<>();

    @OneToOne(mappedBy = "group")
    private CheckPoint checkPoint;

    private String team;

    private String description;

    private Long code;

    private Boolean isOpen;

    private Boolean isEnd;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean isDeleted;

    public void addUserGroup(UserGroup userGroup) {
        this.userGroup.add(userGroup);
        userGroup.addGroup(this);
    }
}
