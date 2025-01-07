package com.bookmile.backend.domain.template.repository;

import com.bookmile.backend.domain.template.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
    @Query("SELECT c FROM Template c WHERE c.group.book.id = :bookId AND c.isTemplate = true ORDER BY c.usageCount DESC")
    List<Template> findPopularTemplatesByBookId(@Param("bookId") Long bookId);
}
