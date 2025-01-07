package com.bookmile.backend.domain.template.service;

import com.bookmile.backend.domain.template.entity.Template;

import java.util.List;

public interface TemplateService {
    List<Template> getPopularTemplates(Long bookId); // 인기 템플릿 조회
}