package com.bookmile.backend.domain.userGroup.controller;

import com.bookmile.backend.domain.group.entity.GroupStatus;
import com.bookmile.backend.domain.userGroup.dto.res.UserGroupSearchResponseDto;
import com.bookmile.backend.domain.userGroup.service.UserGroupService;
import com.bookmile.backend.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.bookmile.backend.global.common.StatusCode.GROUP_LIST_FOUND;

@RestController
@RequestMapping("/api/v1/user-groups")
@RequiredArgsConstructor
public class UserGroupController {
    private final UserGroupService userGroupService;

    @Operation(summary = "사용자 별 참여 그룹 기록을 조회합니다", description = "그룹 상태별로 구분하여 조회합니다.")
    @GetMapping("/my-groups")
    public ResponseEntity<CommonResponse<List<UserGroupSearchResponseDto>>> getUserGroups (
            @RequestParam GroupStatus status,
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        String userEmail = userDetails.getUsername();
        List<UserGroupSearchResponseDto> groups = userGroupService.getUserGroupsByStatus(userEmail, status);
        return ResponseEntity.status(GROUP_LIST_FOUND.getStatus())
                .body(CommonResponse.from(GROUP_LIST_FOUND.getMessage(), groups));
    }
}
