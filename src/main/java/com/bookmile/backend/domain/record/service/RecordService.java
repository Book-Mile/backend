package com.bookmile.backend.domain.record.service;

import com.bookmile.backend.domain.record.dto.req.RecordReqDto;
import com.bookmile.backend.domain.record.dto.req.UpdateRecordReqDto;
import com.bookmile.backend.domain.record.dto.res.RecentRecordResDto;
import com.bookmile.backend.domain.record.dto.res.RecordListResDto;
import java.util.List;

import com.bookmile.backend.domain.record.dto.res.RecordProgressChartDto;
import org.springframework.web.multipart.MultipartFile;

public interface RecordService {
    List<RecordListResDto> viewRecordList(Long groupId, String userEmail);

    Long createRecord(Long groupId, String userEmail, List<MultipartFile> files, RecordReqDto recordReqDto);

    Long updateRecord(Long recordId, UpdateRecordReqDto updateRecordReqDto);

    List<RecentRecordResDto> viewRandomRecord(Long groupId);

    List<RecordProgressChartDto> getProgressChart(Long groupId);

}
