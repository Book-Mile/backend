package com.bookmile.backend.domain.group.controller;

import static com.bookmile.backend.global.common.StatusCode.GROUP_CREATE;
import static com.bookmile.backend.global.common.StatusCode.GROUP_JOIN;

import com.bookmile.backend.domain.group.dto.req.GroupCreateRequestDto;
import com.bookmile.backend.domain.group.dto.req.GroupJoinRequestDto;
import com.bookmile.backend.domain.group.dto.res.GroupCreateResponseDto;
import com.bookmile.backend.domain.group.service.GroupJoinService;
import com.bookmile.backend.domain.group.service.GroupService;
import com.bookmile.backend.global.common.CommonResponse;
import com.bookmile.backend.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final GroupJoinService groupJoinService;

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

    @PostMapping("/{groupId}")
    public ResponseEntity<CommonResponse<Void>> joinGroup(
            @RequestBody @Valid GroupJoinRequestDto requestDto,
            @RequestHeader("user_id") Long userId
    ) {
        groupJoinService.joinGroup(userId, requestDto);
        return ResponseEntity.ok(CommonResponse.from(GROUP_JOIN.getMessage(), null));
    }
}
