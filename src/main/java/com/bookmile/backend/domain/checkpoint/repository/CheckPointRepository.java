package com.bookmile.backend.domain.checkpoint.repository;

import com.bookmile.backend.domain.checkpoint.entity.CheckPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckPointRepository extends JpaRepository<CheckPoint, Long> {
    @Query("SELECT c FROM CheckPoint c WHERE c.group.book.id = :bookId AND c.isTemplate = true ORDER BY c.usageCount DESC")
    List<CheckPoint> findPopularTemplatesByBookId(@Param("bookId") Long bookId);
}
