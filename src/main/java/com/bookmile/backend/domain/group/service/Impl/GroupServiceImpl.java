package com.bookmile.backend.domain.group.service.Impl;

import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.book.service.BookService;
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
        String customGoal = requestDto.getCustomGoal();

        // 2. 템플릿 ID가 있는 경우 템플릿 사용
        if (requestDto.getTemplateId() != null) {
            template = templateRepository.findById(requestDto.getTemplateId())
                    .orElseThrow(() -> new CustomException(INVALID_TEMPLATE_ID));
            goalType = template.getGoalType();
            // 템플릿이 CUSTOM 타입인 경우 customGoal 값을 가져옴
            if (goalType == GoalType.CUSTOM) {
                customGoal = template.getCustomGoal();
            }
            // 템플릿 사용 카운트 증가
            template.increaseUsageCount();
            templateRepository.save(template); // 변경된 값 저장
        } else {
            //템플릿이 없는 경우 GoalType 검증 및 처리
            try {
                goalType = GoalType.valueOf(requestDto.getGoalType());
            } catch (IllegalArgumentException e) {
                throw new CustomException(INVALID_GOAL_TYPE);
            }

            // GoalType이 CUSTOM일 경우 customGoal 검증
            if (goalType == GoalType.CUSTOM && (requestDto.getCustomGoal() == null || requestDto.getCustomGoal().isEmpty())) {
                throw new CustomException(CUSTOM_GOAL_REQUIRED);
            }
        }

        //그룹 생성
        Group group = groupRepository.save(
                Group.builder()
                        .book(book)
                        .groupName(requestDto.getGroupName())
                        .groupType(requestDto.getGroupType())
                        .goalType(goalType != null ? goalType.name() : null) // 템플릿 사용 시 GoalType은 null 처리
                        .customGoal(customGoal) // customGoal 동기화
                        .maxMembers(requestDto.getMaxMembers())
                        .groupDescription(requestDto.getGroupDescription())
                        .password(requestDto.getPassword())
                        .isOpen(Boolean.valueOf(requestDto.getIsOpen()))
                        .isEnd(false)                     .build()
        );

        // 템플릿 생성 및 저장
        if (requestDto.getTemplateId() == null) {
            template = new Template(
                    group, // 그룹 연결
                    goalType,
                    customGoal, // customGoal 전달
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
                .goalType(goalType != null ? goalType.name() : null) // GoalType 반환
                .customGoal(customGoal) // customGoal 포함
                .build();
    }
}