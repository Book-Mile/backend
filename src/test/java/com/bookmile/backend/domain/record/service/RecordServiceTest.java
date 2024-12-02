package com.bookmile.backend.domain.record.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.group.entity.Role;
import com.bookmile.backend.domain.record.dto.RecordListResponse;
import com.bookmile.backend.domain.record.dto.RequestRecord;
import com.bookmile.backend.domain.record.entity.Record;
import com.bookmile.backend.domain.record.repository.RecordRepository;
import com.bookmile.backend.domain.review.service.BookRepository;
import com.bookmile.backend.domain.review.service.UserRepository;
import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.userGroup.entity.UserGroup;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RecordServiceTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private RecordService recordService;

    private Book book;
    private User user;
    private Group group;
    private UserGroup userGroup;

    @BeforeEach
    void setUp() {
        book = new Book("김진용의 인생", 456, "image", "김진용", "책설명", "링크url", 5.0);
        bookRepository.save(book);

        user = new User("김진용", "kje@naver.com", "1234", "urlurl");
        userRepository.save(user);

        group = new Group(book, null, null, "독서 그룹", "독서를 위한 그룹", 1234L, true, false);
        groupRepository.save(group);

        userGroup = new UserGroup(user, group, Role.MASTER);
        userGroupRepository.save(userGroup);
    }

    @Test
    void 사용자의_그룹에서의_기록_생성() {
        //When
        Long recordId1 = recordService.createRecord(group.getId(), user.getId(),
                new RequestRecord("나의 기록1", 193, List.of("url1", "url2", "url3")));
        Record record = recordRepository.findById(recordId1).orElseThrow();

        //Then
        assertEquals("나의 기록1", record.getText());
        assertEquals(193, record.getCurrentPage());
    }

    @Transactional
    @Test
    void 사용자의_그룹에_해당하는_기록_불러오기() {
        //Given
        recordService.createRecord(
                group.getId(),
                user.getId(),
                new RequestRecord("기록1", 193, List.of()));

        recordService.createRecord(
                group.getId(),
                user.getId(),
                new RequestRecord("기록2", 234, List.of("url1", "url2", "url3")));

        //When
        List<RecordListResponse> records = recordService.viewRecordList(group.getId(), user.getId());

        //Then
        assertEquals(2, records.size());
        assertEquals("기록1", records.get(0).getText());
        assertEquals("기록2", records.get(1).getText());
        assertEquals(193, records.get(0).getCurrent_page());
        assertEquals(234, records.get(1).getCurrent_page());
        assertEquals(0, records.get(0).getImages().size());
        assertEquals(3, records.get(1).getImages().size());
    }

    @Transactional
    @Test
    void 기록_수정() {
        //When
        // 일단 생성
        Long recordId = recordService.createRecord(group.getId(), user.getId(),
                new RequestRecord("나의 기록1", 193, List.of()));
        Record record1 = recordRepository.findById(recordId).orElseThrow();

        // 수정 전
        assertEquals("나의 기록1", record1.getText());
        assertEquals(0, record1.getImages().size());

        // 그리고 수정
        Long updateRecord = recordService.updateRecord(recordId,
                new RequestRecord("나의 수정한 기록1", 193, List.of("url1", "url2", "url3")));
        Record record2 = recordRepository.findById(updateRecord).orElseThrow();

        //Then
        assertEquals(record1.getId(), record2.getId());
        // 수정 후
        assertEquals("나의 수정한 기록1", record2.getText());
        assertEquals(3, record2.getImages().size());
    }
}