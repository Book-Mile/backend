package com.bookmile.backend.domain.template.service.Impl;

import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.group.repository.GroupRepository;
import com.bookmile.backend.domain.template.dto.res.TemplateResponseDto;
import com.bookmile.backend.domain.template.entity.GoalType;
import com.bookmile.backend.domain.template.entity.Template;
import com.bookmile.backend.domain.template.repository.TemplateRepository;
import com.bookmile.backend.domain.template.service.TemplateService;
import com.bookmile.backend.global.common.StatusCode;
import com.bookmile.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;
    private final GroupRepository groupRepository;

    @Override
    public List<TemplateResponseDto> getTopTemplatesByBookIdAndGoalType(Long bookId, GoalType goaltype) {
        List<Template> templates = templateRepository.findTop10ByBookIdAndGoalType(
                bookId, goaltype, (Pageable) PageRequest.of(0, 10));

        return templates.stream()
                .map(template -> {
                    Group group = groupRepository.findById(template.getGroup().getId())
                            .orElseThrow(() -> new CustomException(StatusCode.INVALID_GROUP_ID));
                    return TemplateResponseDto.toDto(template, group);
                })
                .collect(Collectors.toList());
    }
}