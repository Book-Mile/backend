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

import static com.bookmile.backend.global.common.StatusCode.AUTHENTICATION_FAILED;

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
        // 1. GoalType 유효성 검사
        GoalType goalType;
        try {
            goalType = GoalType.valueOf(requestDto.getGoalType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 GoalType 값입니다: " + requestDto.getGoalType());
        }

        // 2. GoalType이 CUSTOM일 경우 customGoal 유효성 검사
        if (goalType == GoalType.CUSTOM && (requestDto.getCustomGoal() == null || requestDto.getCustomGoal().isEmpty())) {
            throw new IllegalArgumentException("GoalType이 CUSTOM일 경우 사용자 정의 목표(customGoal)는 필수입니다.");
        }

        // 3. 책 정보 가져오기
        Book book = bookService.saveBook(requestDto.getIsbn13());

        // 4. Group 생성
        Group group = Group.builder()
                .book(book)
                .groupName(requestDto.getGroupName())
                .groupType(requestDto.getGroupType())
                .goalType(requestDto.getGoalType()) // GoalType 저장
                .customGoal(goalType == GoalType.CUSTOM ? requestDto.getCustomGoal() : null) // CUSTOM 목표 동기화
                .maxMembers(requestDto.getMaxMembers())
                .groupDescription(requestDto.getGroupDescription())
                .password(requestDto.getPassword())
                .isOpen(true)
                .isEnd(false)
                .build();
        group = groupRepository.save(group); // 먼저 Group 저장

        // 5. CheckPoint 생성
        CheckPoint checkPoint = new CheckPoint(
                group, // Group과 연결
                goalType, // GoalType Enum 값 전달
                goalType == GoalType.CUSTOM ? requestDto.getCustomGoal() : null, // 사용자 정의 목표 설정
                true
        );
        checkPointRepository.save(checkPoint); // CheckPoint 저장

        // 6. Group에 CheckPoint 연결
        group.setCheckPoint(checkPoint);
        groupRepository.save(group);

        // 7. 그룹 생성자 등록
        UserGroup userGroup = UserGroup.builder()
                .user(user)
                .group(group)
                .role(Role.MASTER)
                .build();
        userGroupRepository.save(userGroup);

        // 8. 그룹 생성 응답 반환
        return GroupCreateResponseDto.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .groupDescription(group.getGroupDescription())
                .maxMembers(group.getMaxMembers())
                .goalType(goalType.name()) // GoalType 이름 반환
                .templateId(requestDto.getTemplateId())
                .customGoal(requestDto.getCustomGoal())
                .build();
    }

}
