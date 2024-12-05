package com.bookmile.backend.domain.record.service;

import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.record.dto.RecordListResponse;
import com.bookmile.backend.domain.record.dto.RequestRecord;
import com.bookmile.backend.domain.record.entity.Record;
import com.bookmile.backend.domain.record.repository.RecordRepository;
import com.bookmile.backend.domain.review.service.UserRepository;
import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.userGroup.entity.UserGroup;
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

    public List<RecordListResponse> viewRecordList(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("없는 그룹입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("없는 사용자입니다."));

        Long userGroupId = userGroupRepository.findUserGroupIdByGroupIdAndUserId(groupId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 그룹에 속하지 않은 사용자입니다."));

        return recordRepository.findAllByUserGroupId(userGroupId).stream()
                .map(RecordListResponse::createRecord)
                .toList();
    }

    public Long createRecord(Long groupId, Long userId, RequestRecord requestRecord) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("없는 그룹입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("없는 사용자입니다."));

        Long userGroupId = userGroupRepository.findUserGroupIdByGroupIdAndUserId(groupId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 그룹에 속하지 않은 사용자입니다."));

        UserGroup userGroup = userGroupRepository.findUserGroupById(userGroupId);

        Record record = Record.from(userGroup, requestRecord);
        recordRepository.save(record);

        return record.getId();
    }

    public Long updateRecord(Long recordId, RequestRecord requestRecord) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("없는 기록입니다."));

        record.update(requestRecord);

        return record.getId();
    }
}