package com.bookmile.backend.domain.group.service;

import com.bookmile.backend.domain.group.dto.req.GroupCreateRequestDto;
import com.bookmile.backend.domain.group.dto.req.GroupSearchRequestDto;
import com.bookmile.backend.domain.group.dto.req.GroupStatusUpdateRequestDto;
import com.bookmile.backend.domain.group.dto.res.GroupCreateResponseDto;
import com.bookmile.backend.domain.group.dto.res.GroupSearchResponseDto;
import com.bookmile.backend.domain.group.dto.res.GroupStatusUpdateResponseDto;
import com.bookmile.backend.domain.user.entity.User;

import java.util.List;

public interface GroupService {
    User getUserById(Long userId); // User 정보 조회
    GroupCreateResponseDto createGroup(GroupCreateRequestDto requestDto, User user);
    GroupStatusUpdateResponseDto updateGroupStatus(Long groupId, GroupStatusUpdateRequestDto requestDto, Long userId);
    List<GroupSearchResponseDto> getGroupsByIsbn13(GroupSearchRequestDto requestDto);
}
