package com.bookmile.backend.domain.record.entity;

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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    public Record(UserGroup userGroup, String text, Integer currentPage) {
        this.userGroup = userGroup;
        this.text = text;
        this.currentPage = currentPage;
    }

    public static Record from(UserGroup userGroup, RecordReqDto recordReqDto) {

        return new Record(
                userGroup,
                recordReqDto.getText(),
                recordReqDto.getCurrentPage()
        );
    }

    public void update(UpdateRecordReqDto updateRecordReqDto) {
        this.text = updateRecordReqDto.getText();
        this.currentPage = updateRecordReqDto.getCurrentPage();
    }
}
