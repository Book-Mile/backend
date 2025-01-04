package com.bookmile.backend.domain.group.controller;

import static com.bookmile.backend.global.common.StatusCode.GROUP_CREATE;

import com.bookmile.backend.domain.group.dto.req.GroupCreateRequestDto;
import com.bookmile.backend.domain.group.dto.res.GroupCreateResponseDto;
import com.bookmile.backend.domain.group.service.GroupService;
import com.bookmile.backend.global.common.CommonResponse;
import com.bookmile.backend.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/create")
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

}
