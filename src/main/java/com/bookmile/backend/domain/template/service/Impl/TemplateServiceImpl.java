package com.bookmile.backend.domain.template.service.Impl;

import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.group.repository.GroupRepository;
import com.bookmile.backend.domain.template.dto.res.TemplateResponseDto;
import com.bookmile.backend.domain.template.entity.GoalType;
import com.bookmile.backend.domain.template.entity.Template;
import com.bookmile.backend.domain.template.repository.TemplateRepository;
import com.bookmile.backend.domain.template.service.TemplateService;
import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.userGroup.repository.UserGroupRepository;
import com.bookmile.backend.global.common.StatusCode;
import com.bookmile.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.bookmile.backend.global.common.StatusCode.GROUP_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;

    @Override
    public List<TemplateResponseDto> getTopTemplatesByBookIdAndGoalType(Long bookId, GoalType goalType) {
        Pageable pageable = PageRequest.of(0, 10);
        List<Template> templates = templateRepository.findTop10ByGroup_Book_IdAndGoalTypeAndIsTemplateTrueOrderByUsageCountDesc(
                bookId, goalType, pageable);

        return templates.stream()
                .map(template -> {
                    Group group = getGroupById(template.getGroup().getId());
                    User master = getGroupMaster(group.getId());
                    return TemplateResponseDto.toDto(template, group, master);
                })
                .collect(Collectors.toList());
    }

    private User getGroupMaster(Long groupId) {
        return userGroupRepository.findMasterByGroupId(groupId)
                .orElseThrow(() -> new CustomException(StatusCode.MASTER_NOT_FOUND)).getUser();
    }

    private Group getGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(StatusCode.INVALID_GROUP_ID));
    }
}