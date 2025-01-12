package com.bookmile.backend.domain.group.service;

import com.bookmile.backend.domain.group.dto.req.GroupJoinRequestDto;
import com.bookmile.backend.domain.group.dto.res.GroupJoinResponseDto;

public interface GroupJoinService {
    GroupJoinResponseDto joinGroup(Long userId, GroupJoinRequestDto groupJoinRequestDto);
}
