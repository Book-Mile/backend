package com.bookmile.backend.domain.group.service.Impl;

import com.bookmile.backend.domain.book.dto.res.BookDetailResponseDto;
import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.book.service.BookService;
import com.bookmile.backend.domain.group.dto.req.GroupSearchRequestDto;
import com.bookmile.backend.domain.group.dto.req.GroupStatusUpdateRequestDto;
import com.bookmile.backend.domain.group.dto.res.GroupSearchResponseDto;
import com.bookmile.backend.domain.group.dto.res.GroupStatusUpdateResponseDto;
import com.bookmile.backend.domain.group.entity.GroupStatus;
import com.bookmile.backend.domain.template.entity.Template;
import com.bookmile.backend.domain.template.entity.GoalType;
import com.bookmile.backend.domain.template.repository.TemplateRepository;
import com.bookmile.backend.domain.group.dto.req.GroupCreateRequestDto;
import com.bookmile.backend.domain.group.dto.res.GroupCreateResponseDto;
import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.group.repository.GroupRepository;
import com.bookmile.backend.domain.group.service.GroupService;
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
        //책 정보 가져오기
        Book book = bookService.saveBook(requestDto.getIsbn13());

        Template template;
        GoalType goalType = null;
        String goalContent = requestDto.getGoalContent();

        // 템플릿 ID가 있는 경우 템플릿 사용
        if (requestDto.getTemplateId() != null) {
            template = templateRepository.findById(requestDto.getTemplateId())
                    .orElseThrow(() -> new CustomException(INVALID_TEMPLATE_ID));

            // 템플릿 검증
            if (template.getGroup().getStatus() != GroupStatus.COMPLETED) {
                throw new CustomException(INVALID_TEMPLATE_USAGE);
            }

            goalType = template.getGoalType();
            goalContent = template.getGoalContent();

            // 템플릿 사용 카운트 증가
            template.increaseUsageCount();
            templateRepository.save(template);
        } else {
            // GoalType 검증 및 처리
            try {
                goalType = GoalType.valueOf(requestDto.getGoalType());
            } catch (IllegalArgumentException e) {
                throw new CustomException(INVALID_GOAL_TYPE);
            }

            // goalContent 검증
            if (goalContent == null || goalContent.isEmpty()) {
                throw new CustomException(GOAL_CONTENT_REQUIRED);
            }
        }

        //그룹 생성
        Group group = groupRepository.save(
                Group.builder()
                        .book(book)
                        .groupName(requestDto.getGroupName())
                        .groupType(requestDto.getGroupType())
                        .goalType(goalType != null ? goalType.name() : null) // 템플릿 사용 시 GoalType은 null 처리
                        .goalContent(goalContent) // goalContent 저장
                        .maxMembers(requestDto.getMaxMembers())
                        .groupDescription(requestDto.getGroupDescription())
                        .password(requestDto.getPassword())
                        .status(GroupStatus.RECRUITING)
                        .isOpen(Boolean.valueOf(requestDto.getIsOpen()))
                        .build()
        );

        // 템플릿 생성 및 저장
        if (requestDto.getTemplateId() == null) {
            template = new Template(
                    group, // 그룹 연결
                    goalType,
                    goalContent,
                    true
            );
            templateRepository.save(template); // 템플릿 저장
        }

        // 그룹 생성자 등록
        UserGroup userGroup = UserGroup.builder()
                .user(user)
                .group(group)
                .role(Role.MASTER)
                .build();
        userGroupRepository.save(userGroup);

        // 그룹 생성 응답 반환
        return GroupCreateResponseDto.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .groupDescription(group.getGroupDescription())
                .maxMembers(group.getMaxMembers())
                .goalType(goalType != null ? goalType.name() : null)
                .goalContent(goalContent)
                .status(GroupStatus.RECRUITING.name()) // 기본 상태 반환
                .build();
    }

    @Override
    public GroupStatusUpdateResponseDto updateGroupStatus(Long groupId, GroupStatusUpdateRequestDto requestDto, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(GROUP_NOT_FOUND));

        UserGroup userGroup = userGroupRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new CustomException(NOT_MEMBER));
        if (userGroup.getRole() != Role.MASTER) {
            throw new CustomException(NO_PERMISSION);
        }

        if (group.getStatus() == GroupStatus.RECRUITING && requestDto.getStatus() == GroupStatus.IN_PROGRESS) {
            group.startGroup();
        } else if (group.getStatus() == GroupStatus.IN_PROGRESS && requestDto.getStatus() == GroupStatus.COMPLETED) {
            group.completeGroup();
        } else {
            throw new CustomException(INVALID_GROUP_STATUS_UPDATE);
        }

        groupRepository.save(group);

        return new GroupStatusUpdateResponseDto(group.getId(), group.getStatus());
    }

    @Override
    public List<GroupSearchResponseDto> getGroupsByIsbn13(GroupSearchRequestDto requestDto) {
        List<Group> groups = groupRepository.findByIsbn13AndStatus(requestDto.getIsbn13(), requestDto.getStatus());

        return groups.stream()
                .map(group -> {
                    String masterNickname = userGroupRepository.findMasterNicknameByGroupId(group.getId())
                            .orElseThrow(() -> new CustomException(INVALID_GROUP));
                    return new GroupSearchResponseDto(
                            group.getId(),
                            group.getGroupName(),
                            group.getGroupDescription(),
                            group.getMaxMembers(),
                            userGroupRepository.countByGroupId(group.getId()),
                            group.getStatus(),
                            new BookDetailResponseDto(group.getBook()),
                            group.getGoalType(),
                            group.getGoalContent(),
                            masterNickname
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public GroupSearchResponseDto getGroupDetail(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(INVALID_GROUP_ID));

        int currentMembers = userGroupRepository.countByGroupId(groupId);

        String masterNickname = userGroupRepository.findMasterNicknameByGroupId(group.getId())
                .orElseThrow(() -> new CustomException(INVALID_GROUP));
        return new GroupSearchResponseDto(
                group.getId(),
                group.getGroupName(),
                group.getGroupDescription(),
                group.getMaxMembers(),
                currentMembers,
                group.getStatus(),
                new BookDetailResponseDto(group.getBook()),
                group.getGoalType(),
                group.getGoalContent(),
                masterNickname
        );
    }
}