package com.bookmile.backend.domain.record.service.Impl;

import static com.bookmile.backend.global.common.StatusCode.GROUP_NOT_FOUND;
import static com.bookmile.backend.global.common.StatusCode.NO_USER_OR_NO_GROUP;
import static com.bookmile.backend.global.common.StatusCode.RECORD_NOT_FOUND;
import static com.bookmile.backend.global.common.StatusCode.USER_NOT_FOUND;

import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.image.repository.ImageRepository;
import com.bookmile.backend.domain.image.service.Impl.ImageServiceImpl;
import com.bookmile.backend.domain.record.dto.req.RecordReqDto;
import com.bookmile.backend.domain.record.dto.req.UpdateRecordReqDto;
import com.bookmile.backend.domain.record.dto.res.RecentRecordResDto;
import com.bookmile.backend.domain.record.dto.res.RecordListResDto;
import com.bookmile.backend.domain.record.entity.Record;
import com.bookmile.backend.domain.record.repository.RecordRepository;
import com.bookmile.backend.domain.record.service.RecordGroupRepository;
import com.bookmile.backend.domain.record.service.RecordService;
import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.user.repository.UserRepository;
import com.bookmile.backend.domain.userGroup.entity.UserGroup;
import com.bookmile.backend.domain.userGroup.repository.UserGroupRepository;
import com.bookmile.backend.global.exception.CustomException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RecordServiceImpl implements RecordService {
    private final UserRepository userRepository;
    private final RecordGroupRepository groupRepository;
    private final RecordRepository recordRepository;
    private final UserGroupRepository userGroupRepository;
    private final ImageRepository imageRepository;
    private final ImageServiceImpl imageService;

    @Override
    public List<RecordListResDto> viewRecordList(Long groupId, String userEmail) {
        Group group = findGroupById(groupId);
        User user = findUserByEmail(userEmail);

        UserGroup userGroup = getUserGroup(user.getId(), group.getId());

        List<Record> records = recordRepository.findAllByUserGroupId(userGroup.getId());
        List<RecordListResDto> result = new ArrayList<>();

        for (Record record : records) {
            List<String> imageUrls = imageService.viewImages(record.getId());
            result.add(RecordListResDto.createRecord(record, imageUrls));
        }

        return result;
    }

    @Override
    @Transactional
    public Long createRecord(Long groupId, String userEmail, List<MultipartFile> files, RecordReqDto recordReqDto) {
        Group group = findGroupById(groupId);
        User user = findUserByEmail(userEmail);

        UserGroup userGroup = getUserGroup(user.getId(), group.getId());

        Record record = recordRepository.save(Record.from(userGroup, recordReqDto));

        log.info("Record 저장 - {}", record);

        if (files != null) {
            imageService.saveImages(record.getId(), files);
        }
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

    /* groupId를 사용해서 userEmail 리스트 가져오기
     * userEmail 리스트 가져오면 갖고 있는 groupId와 리스트 안 userId와 조합 해서
     * userGroupId 가져온다음
     * userGroupId 사용해서 Record 리스트 가져와서
     * Record에서 이미지 저장이 되어있는거 가져오
     * */
    /* 고쳐야함!!!!!!!!!
     *
     * */
    @Override
    public List<RecentRecordResDto> viewRandomRecord(Long groupId) {
        List<String> userEmails = userGroupRepository.findUserEmailRandomSortByGroupId(groupId);
        List<User> users = new ArrayList<>();
        for (String userEmail : userEmails) {
            users.add(findUserByEmail(userEmail));
        }
        List<RecentRecordResDto> recentRecordResDtos = new ArrayList<>();
        Random random = new Random();
        for (User user : users) {
            UserGroup userGroup = getUserGroup(groupId, user.getId());

            List<Record> records = recordRepository.findAllRandomSortByUserGroupId(userGroup.getId());

            for (Record record : records) {
                if (record.getImages().isEmpty()) { // 이미지 리스트 비어있으면 그냥 이미지 없는거로 추가
                    recentRecordResDtos
                            .add(RecentRecordResDto
                                    .createRecentRecord(user, record, null));
                } else {                              // 안 비어있으면 기록의 첫번째 이미지로 추가
                    int randomImageIndex = random.nextInt(record.getImages().size());
                    recentRecordResDtos
                            .add(RecentRecordResDto
                                    .createRecentRecord(user,
                                            record,
                                            record.getImages().get(randomImageIndex).getImageUrl()));
                }
                break;
            }
        }
        return recentRecordResDtos;
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(GROUP_NOT_FOUND));
    }

    private User findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    private UserGroup getUserGroup(Long userId, Long groupId) {
        return userGroupRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new CustomException(NO_USER_OR_NO_GROUP));
    }
}