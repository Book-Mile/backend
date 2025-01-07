package com.bookmile.backend.domain.group.service.Impl;

import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.book.service.BookService;
import com.bookmile.backend.domain.checkpoint.entity.CheckPoint;
import com.bookmile.backend.domain.checkpoint.entity.GoalType;
import com.bookmile.backend.domain.checkpoint.repository.CheckPointRepository;
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
    private final CheckPointRepository checkPointRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(AUTHENTICATION_FAILED));
    }

    @Override
    public GroupCreateResponseDto createGroup(GroupCreateRequestDto requestDto, User user) {
        // 1. 책 정보 가져오기
        Book book = bookService.saveBook(requestDto.getIsbn13());

        CheckPoint checkPoint;
        GoalType goalType = null;
        String customGoal = null;

        // 2. 템플릿 ID가 있는 경우 템플릿 사용
        if (requestDto.getTemplateId() != null) {
            checkPoint = checkPointRepository.findById(requestDto.getTemplateId())
                    .orElseThrow(() -> new CustomException(INVALID_TEMPLATE_ID));
            goalType = checkPoint.getGoalType();
            // 템플릿이 CUSTOM 타입인 경우 customGoal 값을 가져옴
            if (goalType == GoalType.CUSTOM) {
                customGoal = checkPoint.getCustomGoal();
            }
        } else {
            // 3. 템플릿이 없는 경우 GoalType 검증 및 처리
            try {
                goalType = GoalType.valueOf(requestDto.getGoalType());
            } catch (IllegalArgumentException e) {
                throw new CustomException(INVALID_GOAL_TYPE);
            }

            // GoalType이 CUSTOM일 경우 customGoal 검증
            if (goalType == GoalType.CUSTOM && (requestDto.getCustomGoal() == null || requestDto.getCustomGoal().isEmpty())) {
                throw new CustomException(CUSTOM_GOAL_REQUIRED);
            }

            // 새로운 CheckPoint 생성
            checkPoint = new CheckPoint(
                    null, // 그룹 연결은 이후 설정
                    goalType,
                    goalType == GoalType.CUSTOM ? requestDto.getCustomGoal() : null,
                    goalType == GoalType.CUSTOM
            );
            checkPointRepository.save(checkPoint); // CheckPoint 저장
        }

        // 4. 그룹 생성
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
                        .isOpen(true)
                        .isEnd(false)
                        .build()
        );

        // 5. CheckPoint와 그룹 연결
        checkPoint.setGroup(group);
        checkPointRepository.save(checkPoint);

        // 6. 그룹 생성자 등록
        UserGroup userGroup = UserGroup.builder()
                .user(user)
                .group(group)
                .role(Role.MASTER)
                .build();
        userGroupRepository.save(userGroup);

        // 7. 그룹 생성 응답 반환
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
