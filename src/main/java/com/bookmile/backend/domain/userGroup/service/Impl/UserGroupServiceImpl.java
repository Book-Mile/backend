package com.bookmile.backend.domain.userGroup.service.Impl;

import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.group.entity.GroupStatus;
import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.user.repository.UserRepository;
import com.bookmile.backend.domain.userGroup.dto.res.UserGroupSearchResponseDto;
import com.bookmile.backend.domain.userGroup.entity.UserGroup;
import com.bookmile.backend.domain.userGroup.repository.UserGroupRepository;
import com.bookmile.backend.domain.userGroup.service.UserGroupService;
import com.bookmile.backend.global.common.StatusCode;
import com.bookmile.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.bookmile.backend.global.common.StatusCode.INVALID_GROUP;

@Service
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;

    @Override
    public List<UserGroupSearchResponseDto> getUserGroupsByStatus(String userEmail, GroupStatus status) {
        User user = validateUserByEmail(userEmail);

        List<UserGroup> userGroups = userGroupRepository.findGroupsByUserEmailAndStatus(userEmail, status);

        List<Group> groups = userGroups.stream()
                .map(UserGroup::getGroup)
                .toList();

        return groups.stream()
                .map(group -> {
                    int currentMembers = countGroupMembers(group.getId());
                    UserGroup masterUserGroup = findMasterUserGroup(group.getId());
                    User masterUser = masterUserGroup.getUser();

                    return UserGroupSearchResponseDto.toDto(group, currentMembers, masterUser);
                })
                .collect(Collectors.toList());
    }

    private User validateUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(StatusCode.USER_NOT_FOUND));
    }

    private UserGroup findMasterUserGroup(Long groupId) {
        return userGroupRepository.findMasterByGroupId(groupId)
                .orElseThrow(() -> new CustomException(INVALID_GROUP));
    }

    private int countGroupMembers(Long groupId) {
        return userGroupRepository.countByGroupId(groupId);
    }
}
