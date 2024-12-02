package com.bookmile.backend.domain.record.controller;

import com.bookmile.backend.domain.record.dto.RecordListResponse;
import com.bookmile.backend.domain.record.dto.RequestRecord;
import com.bookmile.backend.domain.record.service.RecordService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping
    public ResponseEntity<Long> createRecord(@RequestParam Long groupId,
                                             @RequestParam Long userId, @RequestBody RequestRecord requestRecord) {
        Long recordId = recordService.createRecord(groupId, userId, requestRecord);
        return ResponseEntity.ok(recordId);
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<Long> updateRecord(@PathVariable Long recordId, @RequestBody RequestRecord requestRecord) {
        Long updateRecord = recordService.updateRecord(recordId, requestRecord);
        return ResponseEntity.ok(updateRecord);
    }
}
