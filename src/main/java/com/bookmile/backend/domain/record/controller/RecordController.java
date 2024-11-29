package com.bookmile.backend.domain.record.controller;

import com.bookmile.backend.domain.record.dto.RecordListResponse;
import com.bookmile.backend.domain.record.service.RecordService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/records")
@RequiredArgsConstructor
public class RecordController {
    private RecordService recordService;

    @GetMapping
    public ResponseEntity<List<RecordListResponse>> viewRecordList(@RequestParam Long groupId,
                                                                   @RequestParam Long userId) {
        List<RecordListResponse> records = recordService.viewRecordList(groupId, userId);
        return ResponseEntity.ok(records);
    }
}
