package com.bookmile.backend.domain.record.entity;

import com.bookmile.backend.domain.image.entity.Image;
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
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Record extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usergroup_id")
    private UserGroup userGroup;

    @OneToMany
    @JoinColumn(name = "record_id")
    private List<Image> image = new ArrayList<>();

    @Column
    private String text;

    @Column(nullable = false)
    private Integer currentPage;
}
