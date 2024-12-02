package com.bookmile.backend.domain.record.entity;

import com.bookmile.backend.domain.image.entity.Image;
import com.bookmile.backend.domain.record.dto.RequestRecord;
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

    @OneToMany
    @JoinColumn(name = "record_id")
    private List<Image> images = new ArrayList<>();

    @Column
    private String text;

    @Column(nullable = false)
    private Integer currentPage;

    public Record(UserGroup userGroup, String text, Integer currentPage, List<Image> images) {
        this.userGroup = userGroup;
        this.text = text;
        this.currentPage = currentPage;
        this.images = images;
    }

    public static Record From(UserGroup userGroup, RequestRecord requestRecord) {
        List<Image> imageList = new ArrayList<>();
        for (String imageUrl : requestRecord.getImageUrls()) {
            Image image = new Image(imageUrl);
            imageList.add(image);
        }

        return new Record(
                userGroup,
                requestRecord.getText(),
                requestRecord.getCurrentPage(),
                imageList
        );
    }

    public void update(RequestRecord requestRecord) {
        List<Image> imageList = new ArrayList<>();
        for (String imageUrl : requestRecord.getImageUrls()) {
            Image image = new Image(imageUrl);
            imageList.add(image);
        }
        this.text = requestRecord.getText();
        this.currentPage = requestRecord.getCurrentPage();
        this.images = imageList;
    }
}
