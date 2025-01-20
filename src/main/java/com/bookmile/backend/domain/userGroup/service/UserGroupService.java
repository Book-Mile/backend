package com.bookmile.backend.domain.userGroup.service;

import com.bookmile.backend.domain.group.entity.GroupStatus;
import com.bookmile.backend.domain.userGroup.dto.res.UserGroupSearchResponseDto;

import java.util.List;

public interface UserGroupService {
    List<UserGroupSearchResponseDto> getUserGroupsByStatus(String userEmail, GroupStatus status);
}
