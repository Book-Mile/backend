package com.bookmile.backend.domain.group.service;

import com.bookmile.backend.domain.group.dto.req.GroupCreateRequestDto;
import com.bookmile.backend.domain.group.dto.req.GroupStatusUpdateRequestDto;
import com.bookmile.backend.domain.group.dto.res.GroupCreateResponseDto;
import com.bookmile.backend.domain.group.dto.res.GroupDetailResponseDto;
import com.bookmile.backend.domain.group.dto.res.GroupListResponseDto;
import com.bookmile.backend.domain.group.dto.res.GroupStatusUpdateResponseDto;
import com.bookmile.backend.domain.user.entity.User;

import java.util.List;

public interface GroupService {
    User getUserById(Long userId); // User 정보 조회
    GroupCreateResponseDto createGroup(GroupCreateRequestDto requestDto, String userEmail);
    GroupStatusUpdateResponseDto updateGroupStatus(Long groupId, GroupStatusUpdateRequestDto requestDto, String userEmail);

    List<GroupListResponseDto> getRecruitingGroups(String isbn13);
    List<GroupListResponseDto> getInProgressGroups(String isbn13);
    List<GroupListResponseDto> getCompletedGroups(String isbn13);

    GroupDetailResponseDto getGroupDetail(Long groupId);

    void updateGroupPrivate(Long groupId, Boolean isOpen, Long userId);
}
