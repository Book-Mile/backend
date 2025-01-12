package com.bookmile.backend.domain.group.entity;

import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.template.entity.Template;
import com.bookmile.backend.domain.userGroup.entity.UserGroup;
import com.bookmile.backend.global.config.BaseEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

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
    private Template template;

    @Column(nullable = false)
    private String groupType; // 그룹 구분 (개인/단체)

    @Column(nullable = false)
    private int maxMembers; // 최대 인원수

    @Column(nullable = false)
    private String groupName;

    @Column
    private String groupDescription;

    @Column(nullable = true)
    private String password;  // 비밀번호 (선택 사항)

    @Column(nullable = false)
    private String goalType;

    @Column
    private String goalContent;

    @Column(nullable = false)
    private Boolean isOpen;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GroupStatus status; // 그룹 상태 (모집 중, 진행 중, 종료)

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Builder
    public Group(Book book, List<UserGroup> userGroup, Template template, String groupType, String goalType, int maxMembers, String groupName, String groupDescription,
                 String password, String goalContent, Boolean isOpen, GroupStatus status) {
        this.book = book;
        this.userGroup = userGroup;
        this.template = template;
        this.groupType = groupType;
        this.goalType = goalType;
        this.goalContent = goalContent;
        this.maxMembers = maxMembers;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.password = password;
        this.isOpen = isOpen;
        this.status = status;
    }

    public void startGroup() {
        this.status = GroupStatus.IN_PROGRESS;
    }

    public void completeGroup() {
        this.status = GroupStatus.COMPLETED;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }
}