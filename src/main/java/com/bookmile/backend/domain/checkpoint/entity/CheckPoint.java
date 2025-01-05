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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CheckPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checkpoint_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Enumerated(EnumType.STRING)
    private GoalType goalType;

    private String freeType;

    @Column(nullable = false)
    private boolean isTemplate; // 템플릿 여부

    @Column(nullable = false)
    private int usageCount; // 템플릿 사용 횟수

    public CheckPoint(Group group, GoalType goalType, String freeType, boolean isTemplate) {
        this.group = group;
        this.goalType = goalType;
        this.freeType = freeType;
        this.isTemplate = isTemplate;
        this.usageCount = 0; // 초기값
    }

    public void incrementUsage() {
        this.usageCount++;
    }
}
