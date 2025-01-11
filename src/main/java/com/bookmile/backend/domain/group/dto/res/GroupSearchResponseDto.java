package com.bookmile.backend.domain.group.dto.res;

import com.bookmile.backend.domain.book.dto.res.BookResponseDto;
import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.group.entity.GroupStatus;
import com.bookmile.backend.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupSearchResponseDto {
    private Long groupId;
    private String groupName;
    private String groupDescription;
    private int maxMembers;
    private int currentMembers;
    private GroupStatus status;
    private BookResponseDto book;
    private String goalType;
    private String goalContent;
    private String masterNickname;
    private String masterImage;

    @Builder
    public GroupSearchResponseDto(Long groupId, String groupName, String groupDescription, int maxMembers, int currentMembers,
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

    public static GroupSearchResponseDto toDto(Group group, int currentMembers, User masterUser) {
        return GroupSearchResponseDto.builder()
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