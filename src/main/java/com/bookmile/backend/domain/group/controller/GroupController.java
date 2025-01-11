package com.bookmile.backend.domain.group.controller;

import static com.bookmile.backend.global.common.StatusCode.GROUP_CREATE;
import static com.bookmile.backend.global.common.StatusCode.GROUP_JOIN;

import com.bookmile.backend.domain.group.dto.req.GroupCreateRequestDto;
import com.bookmile.backend.domain.group.dto.req.GroupJoinRequestDto;
import com.bookmile.backend.domain.group.dto.req.GroupSearchRequestDto;
import com.bookmile.backend.domain.group.dto.req.GroupStatusUpdateRequestDto;
import com.bookmile.backend.domain.group.dto.res.GroupCreateResponseDto;
import com.bookmile.backend.domain.group.dto.res.GroupMemberResponseDto;
import com.bookmile.backend.domain.group.dto.res.GroupDetailResponseDto;
import com.bookmile.backend.domain.group.dto.res.GroupStatusUpdateResponseDto;
import com.bookmile.backend.domain.group.service.GroupJoinService;
import com.bookmile.backend.domain.group.service.GroupService;
import com.bookmile.backend.domain.group.service.Impl.GroupMemberServiceImpl;
import com.bookmile.backend.global.common.CommonResponse;
import com.bookmile.backend.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final GroupJoinService groupJoinService;
    private final GroupMemberServiceImpl groupMemberServiceImpl;

    @Operation(summary = "그룹 생성하기", description = "그룹을 생성합니다. 생성을 한 유저는 자동으로 MASTER 역할을 부여받으며 설정한 템플릿 정보가 따로 저장됩니다.")
    @PostMapping
    public ResponseEntity<CommonResponse<GroupCreateResponseDto>> createGroup(
            @RequestBody @Valid GroupCreateRequestDto requestDto,
            @RequestParam Long userId // userId를 직접 받음
    ) {
        // User 정보를 UserRepository에서 조회
        User user = groupService.getUserById(userId);

        // 그룹 생성 요청 처리
        GroupCreateResponseDto responseDto = groupService.createGroup(requestDto, user);
        return ResponseEntity.status(GROUP_CREATE.getStatus())
                .body(CommonResponse.from(GROUP_CREATE.getMessage(), responseDto));
    }

    @Operation(summary = "그룹 참여하기", description = "이미 존재하는 그룹에 참여합니다. 참여한 유저는 자동으로 MEMBER 역할을 부여받으며 비공개 그룹의 경우 비밀번호가 필요합니다. <br>"
    + "공개 그룹의 경우 비밀번호를 null 값이 아닌 공백(그냥 띄어쓰기 한 칸)으로 입력해주세요")
    @PostMapping("/{groupId}")
    public ResponseEntity<CommonResponse<Void>> joinGroup(
            @RequestBody @Valid GroupJoinRequestDto requestDto,
            @RequestHeader("user_id") Long userId
    ) {
        groupJoinService.joinGroup(userId, requestDto);
        return ResponseEntity.ok(CommonResponse.from(GROUP_JOIN.getMessage(), null));
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
            @PathVariable Long groupId,@RequestBody @Valid GroupStatusUpdateRequestDto requestDto,
            @RequestHeader("user-id") Long userId
    ) {
        GroupStatusUpdateResponseDto responseDto = groupService.updateGroupStatus(groupId, requestDto, userId);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "그룹 리스트 조회"
            , description = "그룹 리스트를 조회합니다. 도서 ISBN13을 통해 도서별 그룹을 조회하며 그룹 상태에 따라 조회합니다.")
    @PostMapping("/list")
    public ResponseEntity<List<GroupDetailResponseDto>> getGroupsByIsbn13(
            @RequestBody @Valid GroupSearchRequestDto requestDto
    ) {
        List<GroupDetailResponseDto> groups = groupService.getGroupsByIsbn13(requestDto);
        return ResponseEntity.ok(groups);
    }

    @Operation(summary = "그룹 상세 정보 조회"
            , description = "특정 그룹의 상세 정보를 조회합니다.")
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDetailResponseDto> getGroupDetail(@PathVariable Long groupId) {
        GroupDetailResponseDto groupDetail = groupService.getGroupDetail(groupId);
        return ResponseEntity.ok(groupDetail);
    }
}
