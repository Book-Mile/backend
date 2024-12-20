package com.bookmile.backend.domain.record.controller;

import static com.bookmile.backend.global.common.StatusCode.CREATE_RECORD;
import static com.bookmile.backend.global.common.StatusCode.UPDATE_RECORD;
import static com.bookmile.backend.global.common.StatusCode.VIEW_RECORD;

import com.bookmile.backend.domain.record.dto.req.RecordReqDto;
import com.bookmile.backend.domain.record.dto.req.UpdateRecordReqDto;
import com.bookmile.backend.domain.record.dto.res.RecordListResDto;
import com.bookmile.backend.domain.record.service.RecordService;
import com.bookmile.backend.global.common.CommonResponse;
import jakarta.validation.Valid;
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
    public ResponseEntity<CommonResponse<List<RecordListResDto>>> viewRecordList(@RequestParam Long groupId,
                                                                                 @RequestParam Long userId) {
        List<RecordListResDto> records = recordService.viewRecordList(groupId, userId);
        return ResponseEntity.status(VIEW_RECORD.getStatus())
                .body(CommonResponse.from(VIEW_RECORD.getMessage(), records));
    }

    @PostMapping
    public ResponseEntity<CommonResponse<Long>> createRecord(@RequestParam Long groupId,
                                                             @RequestParam Long userId,
                                                             @Valid @RequestBody RecordReqDto recordReqDto) {
        Long recordId = recordService.createRecord(groupId, userId, recordReqDto);
        return ResponseEntity.status(CREATE_RECORD.getStatus())
                .body(CommonResponse.from(CREATE_RECORD.getMessage(), recordId));
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<CommonResponse<Long>> updateRecord(@PathVariable Long recordId,
                                                             @Valid @RequestBody UpdateRecordReqDto updateRecordReqDto) {
        Long updateRecord = recordService.updateRecord(recordId, updateRecordReqDto);
        return ResponseEntity.status(UPDATE_RECORD.getStatus())
                .body(CommonResponse.from(UPDATE_RECORD.getMessage(), updateRecord));
    }
}
