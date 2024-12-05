package com.bookmile.backend.domain.image.repository;

import com.bookmile.backend.domain.image.entity.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByRecordId(Long recordId);
}
