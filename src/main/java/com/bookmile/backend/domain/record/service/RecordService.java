package com.bookmile.backend.domain.record.service;

import com.bookmile.backend.domain.record.dto.req.RecordReqDto;
import com.bookmile.backend.domain.record.dto.req.UpdateRecordReqDto;
import com.bookmile.backend.domain.record.dto.res.RecordListResDto;
import java.util.List;

public interface RecordService {
    List<RecordListResDto> viewRecordList(Long groupId, Long userId);

    Long createRecord(Long groupId, Long userId, RecordReqDto recordReqDto);

    Long updateRecord(Long recordId, UpdateRecordReqDto updateRecordReqDto);
}
