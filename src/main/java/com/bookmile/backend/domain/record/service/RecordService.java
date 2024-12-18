package com.bookmile.backend.domain.record.service;

import static com.bookmile.backend.global.common.StatusCode.GROUP_NOT_FOUND;
import static com.bookmile.backend.global.common.StatusCode.NO_USER_OR_NO_GROUP;
import static com.bookmile.backend.global.common.StatusCode.RECORD_NOT_FOUND;
import static com.bookmile.backend.global.common.StatusCode.USER_NOT_FOUND;

import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.record.dto.req.RecordReqDto;
import com.bookmile.backend.domain.record.dto.req.UpdateRecordReqDto;
import com.bookmile.backend.domain.record.dto.res.RecordListResDto;
import com.bookmile.backend.domain.record.entity.Record;
import com.bookmile.backend.domain.record.repository.RecordRepository;
import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.user.repository.UserRepository;
import com.bookmile.backend.domain.userGroup.entity.UserGroup;
import com.bookmile.backend.global.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final RecordRepository recordRepository;

    public List<RecordListResDto> viewRecordList(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(GROUP_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Long userGroupId = userGroupRepository.findUserGroupIdByGroupIdAndUserId(groupId, userId)
                .orElseThrow(() -> new CustomException(NO_USER_OR_NO_GROUP));

        return recordRepository.findAllByUserGroupId(userGroupId).stream()
                .map(RecordListResDto::createRecord)
                .toList();
    }

    public Long createRecord(Long groupId, Long userId, RecordReqDto recordReqDto) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(GROUP_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Long userGroupId = userGroupRepository.findUserGroupIdByGroupIdAndUserId(groupId, userId)
                .orElseThrow(() -> new CustomException(NO_USER_OR_NO_GROUP));

        UserGroup userGroup = userGroupRepository.findUserGroupById(userGroupId);

        Record record = Record.from(userGroup, recordReqDto);
        recordRepository.save(record);

        return record.getId();
    }

    public Long updateRecord(Long recordId, UpdateRecordReqDto updateRecordReqDto) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new CustomException(RECORD_NOT_FOUND));

        record.update(updateRecordReqDto);

        return record.getId();
    }
}