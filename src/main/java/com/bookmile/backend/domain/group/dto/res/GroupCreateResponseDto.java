package com.bookmile.backend.domain.group.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GroupCreateResponseDto {
    private Long groupId;
    private String groupName;
    private int maxMembers;
    private String goalType;
    private String groupDescription;
}