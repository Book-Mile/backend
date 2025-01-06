package com.bookmile.backend.domain.checkpoint.entity;


import com.bookmile.backend.domain.group.entity.Group;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CheckPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checkpoint_id")
    private Long id;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Enumerated(EnumType.STRING)
    private GoalType goalType;

    @Column
    private String customGoal; // 사용자 정의 목표 (CUSTOM인 경우)

    @Column(nullable = false)
    private boolean isTemplate; // 템플릿 여부

    @Column(nullable = false)
    private int usageCount = 0; // 템플릿 사용 횟수

    // 생성자
    public CheckPoint(Group group, GoalType goalType, String customGoal, boolean isTemplate) {
        this.group = group;
        this.goalType = goalType;
        this.customGoal = customGoal; // 사용자 정의 목표
        this.isTemplate = isTemplate;
        this.usageCount = isTemplate ? 1 : 0; // 템플릿이면 사용 횟수 1로 초기화
    }

    // 템플릿 사용 횟수 증가 메서드
    public void incrementUsageCount() {
        if (this.isTemplate) {
            this.usageCount++;
        }
    }
}
