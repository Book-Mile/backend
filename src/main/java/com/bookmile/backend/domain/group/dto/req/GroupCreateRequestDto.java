package com.bookmile.backend.domain.group.dto.req;

import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.group.entity.GroupStatus;
import com.bookmile.backend.domain.template.entity.GoalType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class GroupCreateRequestDto {

    @NotBlank(message = "그룹명은 필수입니다.")
    @Schema(description = "그룹명", example = "채식주의자 읽을 사람 모여")
    private String groupName;

    @NotBlank(message = "그룹 타입은 필수입니다.")
    @Schema(description = "그룹 타입", example = "group")
    private String groupType; // 개인 또는 단체

    @NotNull(message = "최대 인원수는 필수입니다.(최대 50)")
    @Schema(description = "최대 인원수", example = "10")
    private Integer maxMembers;

    @Schema(description = "비밀번호(선택사항)", example = "1234")
    private String password; // 선택 사항

    @NotBlank(message = "책 ISBN은 필수입니다.")
    @Schema(description = "도서 ISBN13", example = "9788936434595")
    private String isbn13;

    @NotBlank(message = "그룹 설명을 적어주세요.")
    @Schema(description = "그룹 소개글", example = "방학 안에 다 읽어봐요!")
    private String groupDescription;

    @Schema(description = "템플릿 ID (선택사항)", example = "1")
    private Long templateId; // 템플릿 ID

    @Schema(description = "도서 목표 방식 (선택사항)", example = "PAGE")
    private String goalType; // GoalType (PAGE, CHAPTER, NUMBER, CUSTOM)

    @Schema(description = "사용자 정의 목표 내용 (CUSTOM인 경우 null)", example = "30")
    private String goalContent;


    public Group toEntity(Book book, GoalType goalType, String goalContent) {
        return Group.builder()
                .book(book)
                .groupName(groupName)
                .groupType(groupType)
                .goalType(goalType.name())
                .goalContent(goalContent)
                .maxMembers(maxMembers)
                .groupDescription(groupDescription)
                .password(password)
                .status(GroupStatus.RECRUITING)
                .isOpen(true)
                .build();
    }
}