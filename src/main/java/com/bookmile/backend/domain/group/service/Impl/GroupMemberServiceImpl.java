package com.bookmile.backend.domain.group.service.Impl;

import com.bookmile.backend.domain.group.dto.res.GroupMemberResponseDto;
import com.bookmile.backend.domain.group.service.GroupMemeberService;
import com.bookmile.backend.domain.userGroup.entity.UserGroup;
import com.bookmile.backend.domain.userGroup.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupMemberServiceImpl implements GroupMemeberService {

    private final UserGroupRepository userGroupRepository;

    @Override
    public List<GroupMemberResponseDto> getGroupMembers(Long groupId) {

        List<UserGroup> userGroups = userGroupRepository.findByGroupId(groupId);

        return userGroups.stream()
                .map(userGroup -> new GroupMemberResponseDto(
                        userGroup.getUser().getId(),
                        userGroup.getUser().getNickname(),
                        userGroup.getRole(),
                        userGroup.getUser().getImage()
                )
                )
                .collect(Collectors.toList());
    }
}