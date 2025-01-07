package com.bookmile.backend.domain.template.service.Impl;

import com.bookmile.backend.domain.template.entity.Template;
import com.bookmile.backend.domain.template.repository.TemplateRepository;
import com.bookmile.backend.domain.template.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository checkPointRepository;

    @Override
    public List<Template> getPopularTemplates(Long bookId) {
        return checkPointRepository.findPopularTemplatesByBookId(bookId);
    }
}