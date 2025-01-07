package com.bookmile.backend.domain.group.service;

import com.bookmile.backend.domain.group.dto.req.GroupJoinRequestDto;

public interface GroupJoinService {
    void joinGroup(Long userId, GroupJoinRequestDto groupJoinRequestDto);
}
