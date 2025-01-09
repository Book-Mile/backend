package com.bookmile.backend.domain.group.dto.res;

import com.bookmile.backend.domain.book.dto.res.BookDetailResponseDto;
import com.bookmile.backend.domain.group.entity.GroupStatus;
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
    private BookDetailResponseDto book;
    private String goalType;
    private String goalContent;
    private String masterNickname;

    public GroupSearchResponseDto(Long groupId, String groupName, String groupDescription, int maxMembers, int currentMembers,
                                  GroupStatus status, BookDetailResponseDto book, String goalType, String goalContent, String masterNickname) {
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
    }
}