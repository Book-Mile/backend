package com.bookmile.backend.domain.template.repository;

import com.bookmile.backend.domain.template.entity.GoalType;
import com.bookmile.backend.domain.template.entity.Template;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
    List<Template> findTop10ByGroup_Book_IdAndGoalTypeAndIsTemplateTrueOrderByUsageCountDesc(
            Long bookId, GoalType goalType, Pageable pageable);

}
