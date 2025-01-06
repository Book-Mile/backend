package com.bookmile.backend.domain.checkpoint.controller;

import com.bookmile.backend.domain.checkpoint.entity.CheckPoint;
import com.bookmile.backend.domain.checkpoint.service.CheckPointService;
import com.bookmile.backend.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bookmile.backend.global.common.StatusCode.CHECKPOINT_TEMPLETE;

@RestController
@RequestMapping("/api/v1/templates")
@RequiredArgsConstructor
public class CheckPointController {

    private final CheckPointService checkPointService;

    @GetMapping("/{bookId}")
    public ResponseEntity<CommonResponse<List<CheckPoint>>> getPopularTemplates(@PathVariable Long bookId) {
        List<CheckPoint> templates = checkPointService.getPopularTemplates(bookId);
        return ResponseEntity.ok(CommonResponse.from(CHECKPOINT_TEMPLETE.getMessage(), templates));
    }
}