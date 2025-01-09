package com.bookmile.backend.domain.group.service;

import com.bookmile.backend.domain.group.dto.req.GroupCreateRequestDto;
import com.bookmile.backend.domain.group.dto.req.GroupStatusUpdateRequestDto;
import com.bookmile.backend.domain.group.dto.res.GroupCreateResponseDto;
import com.bookmile.backend.domain.user.entity.User;

public interface GroupService {
    User getUserById(Long userId); // User 정보 조회
    GroupCreateResponseDto createGroup(GroupCreateRequestDto requestDto, User user);
}
