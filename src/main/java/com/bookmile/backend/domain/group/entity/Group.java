package com.bookmile.backend.domain.group.entity;

import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.checkpoint.entity.CheckPoint;
import com.bookmile.backend.domain.userGroup.entity.UserGroup;
import com.bookmile.backend.global.config.BaseEntity;
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
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @OneToMany(mappedBy = "group")
    private List<UserGroup> userGroup = new ArrayList<>();

    @OneToOne(mappedBy = "group")
    private CheckPoint checkPoint;

    @Column(nullable = false)
    private String type; // 그룹 구분 (개인/단체)

    @Column(nullable = false)
    private int maxMembers; // 최대 인원수

    @Column(nullable = false)
    private String groupName;

    @Column
    private String description;

    @Column(nullable = false)
    private String password;  // 비밀번호 (선택 사항)

    @Column(nullable = false)
    private Boolean isOpen;

    @Column(nullable = false)
    private Boolean isEnd;

    @Column(nullable = false)

    private Boolean isDeleted = false;

    @Builder
    public Group(Book book, List<UserGroup> userGroup, CheckPoint checkPoint,String type, int maxMembers, String groupName, String description,
                 String password, Boolean isOpen, Boolean isEnd) {
        this.book = book;
        this.userGroup = userGroup;
        this.checkPoint = checkPoint;
        this.type = type;
        this.maxMembers = maxMembers;
        this.groupName = groupName;
        this.description = description;
        this.password = password;
        this.isOpen = isOpen;
        this.isEnd = isEnd;
    }
}