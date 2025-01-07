package com.bookmile.backend.domain.template.controller;

import com.bookmile.backend.domain.template.entity.Template;
import com.bookmile.backend.domain.template.service.TemplateService;
import com.bookmile.backend.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bookmile.backend.global.common.StatusCode.CHECKPOINT_TEMPLETE;

@RestController
@RequestMapping("/api/v1/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService checkPointService;

    @GetMapping("/{bookId}")
    public ResponseEntity<CommonResponse<List<Template>>> getPopularTemplates(@PathVariable Long bookId) {
        List<Template> templates = checkPointService.getPopularTemplates(bookId);
        return ResponseEntity.ok(CommonResponse.from(CHECKPOINT_TEMPLETE.getMessage(), templates));
    }
}