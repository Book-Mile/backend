package com.bookmile.backend.domain.group.service.Impl;

import com.bookmile.backend.domain.group.dto.req.GroupJoinRequestDto;
import com.bookmile.backend.domain.group.dto.res.GroupJoinResponseDto;
import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.group.repository.GroupRepository;
import com.bookmile.backend.domain.group.service.GroupJoinService;
import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.userGroup.entity.Role;
import com.bookmile.backend.domain.userGroup.entity.UserGroup;
import com.bookmile.backend.domain.userGroup.repository.UserGroupRepository;
import com.bookmile.backend.global.common.StatusCode;
import com.bookmile.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupJoinServiceImpl implements GroupJoinService {

    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;

    @Override
    public GroupJoinResponseDto joinGroup(Long userId, GroupJoinRequestDto groupJoinRequestDto) {

        Group group = findGroup(groupJoinRequestDto.getGroupId());

        checkUserAlreadyJoined(userId, group.getId());
        checkGroupCapacity(group);
        checkGroupPassword(group, groupJoinRequestDto.getPassword());

        // 그룹에 참여
        UserGroup userGroup = UserGroup.builder()
                .user(new User(userId))
                .group(group)
                .role(Role.MEMBER)
                .build();
        userGroupRepository.save(userGroup);

        // DTO 변환 및 반환
        return GroupJoinResponseDto.toDto(group);
    }

    private Group findGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(StatusCode.INVALID_GROUP_ID));
    }

    private void checkUserAlreadyJoined(Long userId, Long groupId) {
        boolean alreadyJoined = userGroupRepository.existsByUserIdAndGroupId(userId, groupId);
        if (alreadyJoined) {
            throw new CustomException(StatusCode.ALREADY_JOINED_GROUP);
        }
    }

    private void checkGroupCapacity(Group group) {
        int currentMemberCount = userGroupRepository.countByGroupId(group.getId());
        if (currentMemberCount >= group.getMaxMembers()) {
            throw new CustomException(StatusCode.GROUP_MEMBER_LIMIT_REACHED);
        }
    }

    private void checkGroupPassword(Group group, String password) {
        if (!group.getIsOpen() && (password == null || !group.getPassword().equals(password))) {
            throw new CustomException(StatusCode.INVALID_GROUP_PASSWORD);
        }
    }
}
