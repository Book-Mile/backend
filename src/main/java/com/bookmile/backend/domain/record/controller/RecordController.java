package com.bookmile.backend.domain.record.controller;

import static com.bookmile.backend.global.common.StatusCode.CREATE_RECORD;
import static com.bookmile.backend.global.common.StatusCode.UPDATE_RECORD;
import static com.bookmile.backend.global.common.StatusCode.VIEW_RECORD;

import com.bookmile.backend.domain.record.dto.req.RecentRecordResDto;
import com.bookmile.backend.domain.record.dto.req.RecordReqDto;
import com.bookmile.backend.domain.record.dto.req.UpdateRecordReqDto;
import com.bookmile.backend.domain.record.dto.res.RecordListResDto;
import com.bookmile.backend.domain.record.service.Impl.RecordServiceImpl;
import com.bookmile.backend.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/records")
@RequiredArgsConstructor
public class RecordController {
    private final RecordServiceImpl recordServiceImpl;

    @Operation(summary = "기록 리스트 조회", description = "해당 그룹의 기록 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonResponse<List<RecordListResDto>>> viewRecordList(@RequestParam Long groupId,
                                                                                 @RequestParam Long userId) {
        List<RecordListResDto> records = recordServiceImpl.viewRecordList(groupId, userId);
        return ResponseEntity.status(VIEW_RECORD.getStatus())
                .body(CommonResponse.from(VIEW_RECORD.getMessage(), records));
    }

    @Operation(summary = "기록 작성", description = "해당 그룹의 기록을 작성합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<Long>> createRecord(@RequestParam Long groupId,
                                                             @RequestParam Long userId,
                                                             @RequestPart(required = false) @Parameter(description = "업로드할 파일 리스트") List<MultipartFile> files,
                                                             @RequestPart("recordReqDto") @Parameter(description = "기록 요청 DTO (JSON)") @Valid RecordReqDto recordReqDto) {
        if (files == null) {
            files = new ArrayList<>();
        }
        Long recordId = recordServiceImpl.createRecord(groupId, userId, files, recordReqDto);
        return ResponseEntity.status(CREATE_RECORD.getStatus())
                .body(CommonResponse.from(CREATE_RECORD.getMessage(), recordId));
    }

    @Operation(summary = "기록 수정", description = "기록을 수정합니다.")
    @PutMapping("/{recordId}")
    public ResponseEntity<CommonResponse<Long>> updateRecord(@PathVariable Long recordId,
                                                             @Valid @RequestBody UpdateRecordReqDto updateRecordReqDto) {
        Long updateRecord = recordServiceImpl.updateRecord(recordId, updateRecordReqDto);
        return ResponseEntity.status(UPDATE_RECORD.getStatus())
                .body(CommonResponse.from(UPDATE_RECORD.getMessage(), updateRecord));
    }

    @Operation(summary = "글 2 사진 2", description = "해당 그룹의 랜덤한 사람의 랜덤한 기록의 사진과 글들 반환합니다")
    @GetMapping("/random")
    public ResponseEntity<CommonResponse<List<RecentRecordResDto>>> viewRandomRecord(@RequestParam Long groupId) {
        List<RecentRecordResDto> recentRecordResDtos = recordServiceImpl.viewRandomRecord(groupId);
        return ResponseEntity.status(VIEW_RECORD.getStatus())
                .body(CommonResponse.from(VIEW_RECORD.getMessage(), recentRecordResDtos));
    }
}