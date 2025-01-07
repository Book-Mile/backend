package com.bookmile.backend.domain.group.service.Impl;

import com.bookmile.backend.domain.group.dto.req.GroupJoinRequestDto;
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

import static ch.qos.logback.core.joran.JoranConstants.NULL;

@Service
@RequiredArgsConstructor
public class GroupJoinServiceImpl implements GroupJoinService {
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;

    @Override
    public void joinGroup(Long userId, GroupJoinRequestDto groupJoinRequestDto) {
        //그룹 가져오기
        Group group = groupRepository.findById(groupJoinRequestDto.getGroupId())
                .orElseThrow(()-> new CustomException(StatusCode.INPUT_VALUE_INVALID));

        //이미 참여한 그룹인가?
        boolean alreadyJoined = userGroupRepository.existsByUserIdAndGroupId(userId, group.getId());
        if (alreadyJoined) {
            throw new CustomException(StatusCode.INPUT_VALUE_INVALID);
        }

        //최대 인원 초과 설정
        int currentMemberCount = userGroupRepository.countByGroupId(group.getId());
        if (currentMemberCount >= group.getMaxMembers()) {
            throw new CustomException(StatusCode.INPUT_VALUE_INVALID);
        }

        //비공개 그룹일 경우 비밀번호
        if (!group.getIsOpen() && (groupJoinRequestDto.getPassword() == NULL
                || !group.getPassword().equals(groupJoinRequestDto.getPassword()))) {
            throw new CustomException(StatusCode.INPUT_VALUE_INVALID);
        }

        //그룹에 참여하기
        UserGroup userGroup = UserGroup.builder()
                .user(new User(userId))
                .group(group)
                .role(Role.MEMBER)
                .build();
        userGroupRepository.save(userGroup);
    }
}
