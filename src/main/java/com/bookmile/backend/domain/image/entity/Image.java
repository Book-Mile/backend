package com.bookmile.backend.domain.image.entity;

import com.bookmile.backend.domain.record.entity.Record;
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
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private Record record;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Boolean isDeleted = Boolean.FALSE;

    public void addRecord(Record record) {
        this.record = record;
    }

    public Image(Record record, String imageUrl) {
        this.record = record;
        this.imageUrl = imageUrl;
    }

    public void delete(Image image) {
        this.isDeleted = Boolean.TRUE;
    }
}
