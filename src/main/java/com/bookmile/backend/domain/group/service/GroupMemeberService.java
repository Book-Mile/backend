package com.bookmile.backend.domain.group.service;

import com.bookmile.backend.domain.group.dto.res.GroupMemberResponseDto;

import java.util.List;

public interface GroupMemeberService {
    List<GroupMemberResponseDto> getGroupMembers(Long groupId);
}
