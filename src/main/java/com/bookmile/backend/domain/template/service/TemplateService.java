package com.bookmile.backend.domain.template.service;

import com.bookmile.backend.domain.template.dto.res.TemplateResponseDto;
import com.bookmile.backend.domain.template.entity.GoalType;

import java.util.List;

public interface TemplateService {
    List<TemplateResponseDto> getTopTemplatesByBookIdAndGoalType(Long bookId, GoalType goalType);
}