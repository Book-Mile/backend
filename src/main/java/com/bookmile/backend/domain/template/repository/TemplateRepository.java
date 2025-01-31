package com.bookmile.backend.domain.template.repository;

import com.bookmile.backend.domain.template.entity.GoalType;
import com.bookmile.backend.domain.template.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
    @Query("SELECT t FROM Template t WHERE t.group.book.id = :bookId AND t.goalType = :goalType AND t.isTemplate = true ORDER BY t.usageCount DESC")
    List<Template> findTop10ByBookIdAndGoalType(@Param("bookId") Long bookId, @Param("goalType") GoalType goalType, Pageable pageable);
}
