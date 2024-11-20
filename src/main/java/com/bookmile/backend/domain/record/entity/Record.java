package com.bookmile.backend.domain.record.entity;

import com.bookmile.backend.domain.image.entity.Image;
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
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usergroup_id")
    private UserGroup userGroup;

    @Column
    @OneToMany(mappedBy = "record")
    private List<Image> image;

    @Column
    private String text;

    @Column
    private Integer currentPage;
}
