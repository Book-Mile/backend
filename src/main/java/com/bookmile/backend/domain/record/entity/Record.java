package com.bookmile.backend.domain.record.entity;

import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.image.entity.Image;
import com.bookmile.backend.domain.record.dto.req.RecordReqDto;
import com.bookmile.backend.domain.record.dto.req.UpdateRecordReqDto;
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
import lombok.Builder;
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
    @JoinColumn(name = "usergroup_id", nullable = false)
    private UserGroup userGroup;

    @Column
    private String text;

    @Column(nullable = false)
    private Integer currentPage;

    // 추후, OneToMany는 N+1 문제 일으킬 가능성 있으므로 뺴기로.
    @OneToMany(mappedBy = "record")
    private List<Image> images = new ArrayList<>();

    @Builder
    public Record(UserGroup userGroup, String text, Integer currentPage) {
        this.userGroup = userGroup;
        this.text = text;
        this.currentPage = currentPage;
    }

    public static Record from(UserGroup userGroup, RecordReqDto recordReqDto) {
        return Record.builder()
                .userGroup(userGroup)
                .text(recordReqDto.getText())
                .currentPage(recordReqDto.getCurrentPage())
                .build();
    }

    public void update(UpdateRecordReqDto updateRecordReqDto) {
        this.text = updateRecordReqDto.getText();
        this.currentPage = updateRecordReqDto.getCurrentPage();
    }

}
