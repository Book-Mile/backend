package com.bookmile.backend.domain.record.service.Impl;

import static com.bookmile.backend.global.common.StatusCode.GROUP_NOT_FOUND;
import static com.bookmile.backend.global.common.StatusCode.NO_USER_OR_NO_GROUP;
import static com.bookmile.backend.global.common.StatusCode.RECORD_NOT_FOUND;
import static com.bookmile.backend.global.common.StatusCode.USER_NOT_FOUND;

import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.image.service.Impl.ImageServiceImpl;
import com.bookmile.backend.domain.record.dto.req.RecordReqDto;
import com.bookmile.backend.domain.record.dto.req.UpdateRecordReqDto;
import com.bookmile.backend.domain.record.dto.res.RecordListResDto;
import com.bookmile.backend.domain.record.entity.Record;
import com.bookmile.backend.domain.record.repository.RecordRepository;
import com.bookmile.backend.domain.record.service.RecordGroupRepository;
import com.bookmile.backend.domain.record.service.RecordService;
import com.bookmile.backend.domain.record.service.RecordUserGroupRepository;
import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.user.repository.UserRepository;
import com.bookmile.backend.domain.userGroup.entity.UserGroup;
import com.bookmile.backend.global.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {
    private final UserRepository userRepository;
    private final RecordGroupRepository groupRepository;
    private final RecordUserGroupRepository userGroupRepository;
    private final RecordRepository recordRepository;
    private final ImageServiceImpl imageService;

    @Override
    public List<RecordListResDto> viewRecordList(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(GROUP_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Long userGroupId = userGroupRepository.findUserGroupIdByGroupIdAndUserId(group.getId(), user.getId())
                .orElseThrow(() -> new CustomException(NO_USER_OR_NO_GROUP));

        return recordRepository.findAllByUserGroupId(userGroupId).stream()
                .map(RecordListResDto::createRecord)
                .toList();
    }

    @Override
    public Long createRecord(Long groupId, Long userId, List<MultipartFile> files, RecordReqDto recordReqDto) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(GROUP_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Long userGroupId = userGroupRepository.findUserGroupIdByGroupIdAndUserId(group.getId(), user.getId())
                .orElseThrow(() -> new CustomException(NO_USER_OR_NO_GROUP));

        UserGroup userGroup = userGroupRepository.findUserGroupById(userGroupId);

        Record record = Record.from(userGroup, recordReqDto);
        recordRepository.save(record);

        imageService.saveImages(record.getId(), files);

        return record.getId();
    }

    @Transactional
    @Override
    public Long updateRecord(Long recordId, UpdateRecordReqDto updateRecordReqDto) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new CustomException(RECORD_NOT_FOUND));

        record.update(updateRecordReqDto);

        return record.getId();
    }
}