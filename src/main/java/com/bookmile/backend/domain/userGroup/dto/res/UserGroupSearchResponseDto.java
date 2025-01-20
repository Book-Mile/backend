package com.bookmile.backend.domain.userGroup.dto.res;

import com.bookmile.backend.domain.book.dto.res.BookResponseDto;
import com.bookmile.backend.domain.group.dto.res.GroupDetailResponseDto;
import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.group.entity.GroupStatus;
import com.bookmile.backend.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserGroupSearchResponseDto {
    private final Long groupId;
    private final String groupName;
    private final String groupDescription;
    private final int maxMembers;
    private final int currentMembers;
    private final GroupStatus status;
    private final BookResponseDto book;
    private final String goalType;
    private final String goalContent;
    private final String masterNickname;
    private final String masterImage;

    @Builder
    private UserGroupSearchResponseDto(Long groupId, String groupName, String groupDescription, int maxMembers, int currentMembers,
                                   GroupStatus status, BookResponseDto book, String goalType,
                                   String goalContent, String masterNickname, String masterImage) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.maxMembers = maxMembers;
        this.currentMembers = currentMembers;
        this.status = status;
        this.book = book;
        this.goalType = goalType;
        this.goalContent = goalContent;
        this.masterNickname = masterNickname;
        this.masterImage = masterImage;
    }

    public static UserGroupSearchResponseDto toDto(Group group, int currentMembers, User masterUser) {
        return UserGroupSearchResponseDto.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .groupDescription(group.getGroupDescription())
                .maxMembers(group.getMaxMembers())
                .currentMembers(currentMembers)
                .status(GroupStatus.valueOf(group.getStatus().toString()))
                .book(new BookResponseDto(group.getBook()))
                .goalType(group.getGoalType())
                .goalContent(group.getGoalContent())
                .masterNickname(masterUser.getNickname())
                .masterImage(masterUser.getImage())
                .build();
    }
}
