package com.bookmile.backend.domain.template.controller;

import com.bookmile.backend.domain.template.dto.res.TemplateResponseDto;
import com.bookmile.backend.domain.template.entity.Template;
import com.bookmile.backend.domain.template.service.TemplateService;
import com.bookmile.backend.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.bookmile.backend.global.common.StatusCode.CHECKPOINT_TEMPLETE;

@RestController
@RequestMapping("/api/v1/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @GetMapping("/{bookId}")
    public ResponseEntity<List<TemplateResponseDto>> getPopularTemplatesByBook(@PathVariable Long bookId) {
        List<Template> templates = templateService.getTopTemplatesByBook(bookId);

        List<TemplateResponseDto> responseDtos = templates.stream()
                .map(TemplateResponseDto::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }
}