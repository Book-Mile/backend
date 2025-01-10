package com.bookmile.backend.domain.template.entity;

import com.bookmile.backend.domain.group.entity.Group;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Long id;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Enumerated(EnumType.STRING)
    private GoalType goalType;

    @Column
    private String goalContent;

    @Column(nullable = false)
    private boolean isTemplate; // 템플릿 공유 여부

    @Column(nullable = false)
    private int usageCount = 1; // 템플릿 사용 횟수

    // 생성자
    @Builder
    public Template(Group group, GoalType goalType, String goalContent, boolean isTemplate) {
        this.group = group;
        this.goalType = goalType;
        this.goalContent = goalContent;
        this.isTemplate = isTemplate;
    }

    // 템플릿 사용 횟수 증가 메서드
    public void increaseUsageCount() {
            this.usageCount++;
    }
}
