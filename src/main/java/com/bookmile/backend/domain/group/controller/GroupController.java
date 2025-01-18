package com.bookmile.backend.domain.group.controller;

import com.bookmile.backend.domain.group.dto.req.*;
import com.bookmile.backend.domain.group.dto.res.*;
import com.bookmile.backend.domain.group.service.GroupJoinService;
import com.bookmile.backend.domain.group.service.GroupService;
import com.bookmile.backend.domain.group.service.Impl.GroupMemberServiceImpl;
import com.bookmile.backend.global.common.CommonResponse;
import com.bookmile.backend.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bookmile.backend.global.common.StatusCode.*;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final GroupJoinService groupJoinService;
    private final GroupMemberServiceImpl groupMemberServiceImpl;

    @Operation(summary = "그룹 생성하기", description = "그룹을 생성합니다. 비밀번호를 설정하지 않는 그룹의 경우 비밀번호를 띄어쓰기 한 칸으로 입력해주세요.<br>" +
            "템플릿 공유를 사용할 경우 템플릿 아이디는 숫자로 (ex : 1) 나머지는 null 값으로 입력해주세요. <br>" +
            "템플릿 공유 없이 생성 할 경우 템플릿 아이디는 null 값으로 GoalType 과 GoalContent는 필수 입력입니다. <br>" +
            "생성을 한 유저는 자동으로 MASTER 역할을 부여받으며 설정한 템플릿 정보가 따로 저장됩니다.")
    @PostMapping
    public ResponseEntity<CommonResponse<GroupCreateResponseDto>> createGroup(
            @RequestBody @Valid GroupCreateRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userEmail = userDetails.getUsername();

        // 그룹 생성 요청 처리
        GroupCreateResponseDto responseDto = groupService.createGroup(requestDto, userEmail);
        return ResponseEntity.status(GROUP_CREATE.getStatus())
                .body(CommonResponse.from(GROUP_CREATE.getMessage(), responseDto));
    }

    @Operation(summary = "그룹 참여하기", description = "이미 존재하는 그룹에 참여합니다. 참여한 유저는 자동으로 MEMBER 역할을 부여받으며 비공개 그룹의 경우 비밀번호가 필요합니다. <br>"
    + "공개 그룹의 경우 비밀번호를 null 값이 아닌 공백(그냥 띄어쓰기 한 칸)으로 입력해주세요")
    @PostMapping("/{groupId}")
    public ResponseEntity<CommonResponse<GroupJoinResponseDto>> joinGroup(
            @RequestBody @Valid GroupJoinRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userEmail = userDetails.getUsername();
        GroupJoinResponseDto responseDto = groupJoinService.joinGroup(userEmail, requestDto);
        return ResponseEntity.ok(CommonResponse.from(GROUP_JOIN.getMessage(), responseDto));
    }

    @Operation(summary = "그룹 멤버 조회"
            , description = "전체 그룹원을 조회합니다. userId, 닉네임과 직책을 조회할 수 있습니다.")
    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMemberResponseDto>> getMembers(@PathVariable Long groupId) {
        List<GroupMemberResponseDto> members = groupMemberServiceImpl.getGroupMembers(groupId);
        return ResponseEntity.ok(members);
    }

    @Operation(summary = "그룹 상태 변경 (RECRUITING, IN_PROGRESS, COMPLETED)"
            , description = "그룹 상태를 변경합니다. 그룹장만이 변경할 수 있습니다.")
    @PatchMapping("/{groupId}")
    public ResponseEntity<GroupStatusUpdateResponseDto> updateGroupStatus(
            @PathVariable Long groupId,
            @RequestBody @Valid GroupStatusUpdateRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userEmail = userDetails.getUsername();
        GroupStatusUpdateResponseDto responseDto = groupService.updateGroupStatus(groupId, requestDto, userEmail);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "그룹 리스트 조회 (모집 중)", description = "도서 ISBN13으로 모집 중인 그룹을 조회합니다.")
    @GetMapping("/list/recruiting")
    public ResponseEntity<CommonResponse<List<GroupListResponseDto>>> getRecruitingGroups(
            @RequestParam String isbn13
    ) {
        List<GroupListResponseDto> groups = groupService.getRecruitingGroups(isbn13);
        return ResponseEntity.status(GROUP_LIST_FOUND.getStatus())
                .body(CommonResponse.from(GROUP_LIST_FOUND.getMessage(), groups));
    }

    @Operation(summary = "그룹 리스트 조회 (진행 중)", description = "도서 ISBN13으로 진행 중인 그룹을 조회합니다.")
    @GetMapping("/list/in-progress")
    public ResponseEntity<CommonResponse<List<GroupListResponseDto>>> getInProgressGroups(
            @RequestParam String isbn13
    ) {
        List<GroupListResponseDto> groups = groupService.getInProgressGroups(isbn13);
        return ResponseEntity.status(GROUP_LIST_FOUND.getStatus())
                .body(CommonResponse.from(GROUP_LIST_FOUND.getMessage(), groups));
    }

    @Operation(summary = "그룹 리스트 조회 (완료)", description = "도서 ISBN13으로 완료된 그룹을 조회합니다.")
    @GetMapping("/list/completed")
    public ResponseEntity<CommonResponse<List<GroupListResponseDto>>> getCompletedGroups(
            @RequestParam String isbn13
    ) {
        List<GroupListResponseDto> groups = groupService.getCompletedGroups(isbn13);
        return ResponseEntity.status(GROUP_LIST_FOUND.getStatus())
                .body(CommonResponse.from(GROUP_LIST_FOUND.getMessage(), groups));
    }

    @Operation(summary = "그룹 상세 정보 조회"
            , description = "특정 그룹의 상세 정보를 조회합니다.")
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDetailResponseDto> getGroupDetail(@PathVariable Long groupId) {
        GroupDetailResponseDto groupDetail = groupService.getGroupDetail(groupId);
        return ResponseEntity.ok(groupDetail);
    }

    @PatchMapping("/{groupId}/private")
    @Operation(summary = "그룹 공개/비공개 전환", description = "그룹장은 그룹 공개여부를 변경할 수 있습니다.")
    public ResponseEntity<CommonResponse<Object>> updateGroupVisibility(
            @PathVariable Long groupId,
            @RequestBody @Valid GroupPrivateRequestDto requestDto
    ) {
        groupService.updateGroupPrivate(groupId, requestDto.getIsOpen(), requestDto.getUserId());
        return ResponseEntity.ok(CommonResponse.from(GROUP_PRIVATE_UPDATE.getMessage()));
    }
}
