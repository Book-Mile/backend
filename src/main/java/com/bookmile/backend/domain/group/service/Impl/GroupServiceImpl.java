package com.bookmile.backend.domain.group.service.Impl;

import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.book.service.BookService;
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
    private final BookService bookService; // BookService를 통해 책 정보 확인 및 저장
    private final UserGroupRepository userGroupRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(AUTHENTICATION_FAILED)); // 예외 변경
    }

    @Override
    public GroupCreateResponseDto createGroup(GroupCreateRequestDto requestDto, User user) {
        // 1. ISBN으로 책 정보 가져오기
        Book book = bookService.saveBook(requestDto.getIsbn13());

        // 2. 그룹 생성
        Group group = groupRepository.save(
                Group.builder()
                        .book(book)
                        .groupName(requestDto.getGroupName())
                        .maxMembers(requestDto.getMaxMembers())
                        .description(requestDto.getDescription())
                        .password(requestDto.getPassword())
                        .isOpen(true)
                        .isEnd(false)
                        .build()
        );

        // 3. 그룹 생성자 추가
        UserGroup userGroup = UserGroup.builder()
                .user(user)
                .group(group)
                .role(Role.MASTER) // 그룹 생성자
                .build();

        userGroupRepository.save(userGroup);

        // 4. 응답 DTO 반환
        return GroupCreateResponseDto.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .build();
    }
}
