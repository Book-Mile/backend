package com.bookmile.backend.domain.group.service.Impl;

import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.book.service.BookService;
import com.bookmile.backend.domain.group.dto.req.GroupStatusUpdateRequestDto;
import com.bookmile.backend.domain.group.dto.res.GroupDetailResponseDto;
import com.bookmile.backend.domain.group.dto.res.GroupListResponseDto;
import com.bookmile.backend.domain.group.dto.res.GroupStatusUpdateResponseDto;
import com.bookmile.backend.domain.group.entity.GroupStatus;
import com.bookmile.backend.domain.group.dto.req.GroupCreateRequestDto;
import com.bookmile.backend.domain.group.dto.res.GroupCreateResponseDto;
import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.group.repository.GroupRepository;
import com.bookmile.backend.domain.group.service.GroupService;
import com.bookmile.backend.domain.template.entity.Template;
import com.bookmile.backend.domain.template.entity.GoalType;
import com.bookmile.backend.domain.template.repository.TemplateRepository;
import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.user.repository.UserRepository;
import com.bookmile.backend.domain.userGroup.entity.UserGroup;
import com.bookmile.backend.domain.userGroup.entity.Role;
import com.bookmile.backend.domain.userGroup.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.bookmile.backend.global.exception.CustomException;

import java.util.List;
import java.util.stream.Collectors;

import static com.bookmile.backend.global.common.StatusCode.*;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final BookService bookService;
    private final UserGroupRepository userGroupRepository;
    private final TemplateRepository templateRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(AUTHENTICATION_FAILED));
    }

    @Override
    public GroupCreateResponseDto createGroup(GroupCreateRequestDto requestDto, User user) {
        Book book = bookService.saveBook(requestDto.getIsbn13());

        Template template = checkTemplate(requestDto);
        GoalType goalType = checkGoalType(requestDto, template);
        String goalContent = checkGoalContent(requestDto, template);

        Group group = groupRepository.save(requestDto.toEntity(book, goalType, goalContent));

        if (requestDto.getTemplateId() == null) {
            template = Template.builder()
                    .group(group)
                    .goalType(goalType)
                    .goalContent(goalContent)
                    .isTemplate(true)
                    .build();
            templateRepository.save(template);
        }
        registerGroupCreator(user, group);

        return GroupCreateResponseDto.toDto(group, template);
    }

    @Override
    public GroupStatusUpdateResponseDto updateGroupStatus(Long groupId, GroupStatusUpdateRequestDto requestDto, Long userId) {
        Group group = findGroupById(groupId);
        UserGroup userGroup = findUserGroupById(userId, groupId);

        validateGroupMaster(userGroup);
        updateGroupStatus(group, requestDto.getStatus());

        groupRepository.save(group);
        return new GroupStatusUpdateResponseDto(group.getId(), group.getStatus());
    }

    private List<GroupListResponseDto> findGroupsByStatus(String isbn13, GroupStatus status, boolean isRecent) {
        List<Group> groups;
        if (isRecent) {
            groups = groupRepository.findTop4RecentGroupsByIsbn13AndStatus(isbn13, status);
        } else {
            groups = groupRepository.findRandom4GroupsByIsbn13AndStatus(isbn13, status);
        }

        return groups.stream()
                .map(group -> {
                    UserGroup masterUserGroup = findMasterUserGroup(group.getId());
                    User masterUser = masterUserGroup.getUser();
                    int currentMembers = userGroupRepository.countByGroupId(group.getId());
                    return GroupListResponseDto.toDto(group, currentMembers, masterUser);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupListResponseDto> getRecruitingGroups(String isbn13) {
        return findGroupsByStatus(isbn13, GroupStatus.RECRUITING, true);
    }

    @Override
    public List<GroupListResponseDto> getInProgressGroups(String isbn13) {
        return findGroupsByStatus(isbn13, GroupStatus.IN_PROGRESS, false);
    }

    @Override
    public List<GroupListResponseDto> getCompletedGroups(String isbn13) {
        return findGroupsByStatus(isbn13, GroupStatus.COMPLETED, false);
    }

    @Override
    public GroupDetailResponseDto getGroupDetail(Long groupId) {
        Group group = findGroupById(groupId);
        int currentMembers = countGroupMembers(groupId);
        UserGroup masterUserGroup = findMasterUserGroup(group.getId());
        User masterUser = masterUserGroup.getUser();

        return GroupDetailResponseDto.toDto(group, currentMembers, masterUser);
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(GROUP_NOT_FOUND));
    }

    private UserGroup findUserGroupById(Long userId, Long groupId) {
        return userGroupRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new CustomException(NOT_MEMBER));
    }

    private UserGroup findMasterUserGroup(Long groupId) {
        return userGroupRepository.findMasterByGroupId(groupId)
                .orElseThrow(() -> new CustomException(INVALID_GROUP));
    }

    private int countGroupMembers(Long groupId) {
        return userGroupRepository.countByGroupId(groupId);
    }

    private void validateGroupMaster(UserGroup userGroup) {
        if (userGroup.getRole() != Role.MASTER) {
            throw new CustomException(NO_PERMISSION);
        }
    }

    private void updateGroupStatus(Group group, GroupStatus status) {
        if (group.getStatus() == GroupStatus.RECRUITING && status == GroupStatus.IN_PROGRESS) {
            group.startGroup();
        } else if (group.getStatus() == GroupStatus.IN_PROGRESS && status == GroupStatus.COMPLETED) {
            group.completeGroup();
        } else {
            throw new CustomException(INVALID_GROUP_STATUS_UPDATE);
        }
    }

    private Template checkTemplate(GroupCreateRequestDto requestDto) {
        if (requestDto.getTemplateId() != null) {
            Template template = templateRepository.findById(requestDto.getTemplateId())
                    .orElseThrow(() -> new CustomException(INVALID_TEMPLATE_ID));
            if (template.getGroup().getStatus() != GroupStatus.COMPLETED) {
                throw new CustomException(INVALID_TEMPLATE_USAGE);
            }
            template.increaseUsageCount();
            templateRepository.save(template);
            return template;
        }
        return null;
    }

    private GoalType checkGoalType(GroupCreateRequestDto requestDto, Template template) {
        if (template != null) {
            return template.getGoalType();
        }
        try {
            return GoalType.valueOf(requestDto.getGoalType());
        } catch (IllegalArgumentException e) {
            throw new CustomException(INVALID_GOAL_TYPE);
        }
    }

    private String checkGoalContent(GroupCreateRequestDto requestDto, Template template) {
        if (template != null) {
            return template.getGoalContent();
        }
        if (requestDto.getGoalContent() == null || requestDto.getGoalContent().isEmpty()) {
            throw new CustomException(GOAL_CONTENT_REQUIRED);
        }
        return requestDto.getGoalContent();
    }

    private void registerGroupCreator(User user, Group group) {
        UserGroup userGroup = UserGroup.builder()
                .user(user)
                .group(group)
                .role(Role.MASTER)
                .build();
        userGroupRepository.save(userGroup);
    }
}
