package com.bookmile.backend.domain.record.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.group.entity.Role;
import com.bookmile.backend.domain.record.dto.RecordListResponse;
import com.bookmile.backend.domain.record.entity.Record;
import com.bookmile.backend.domain.record.repository.RecordRepository;
import com.bookmile.backend.domain.review.service.BookRepository;
import com.bookmile.backend.domain.review.service.UserRepository;
import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.userGroup.entity.UserGroup;
import java.util.List;
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

    @Test
    void 사용자의_그룹에_해당하는_기록_불러오기() {
        //Given
        Book book = new Book("김진용의 인생", 456, "image", "김진용", "책설명", "링크url", 5.0);
        bookRepository.save(book);

        User user = new User("김진용", "kje@naver.com", "1234", "urlurl");
        userRepository.save(user);

        Group group = new Group(book, null, null, "독서 그룹", "독서를 위한 그룹", 1234L, true, false);
        groupRepository.save(group);

        UserGroup userGroup = new UserGroup(user, group, Role.MASTER);
        userGroupRepository.save(userGroup);

        Record record1 = new Record(userGroup, "첫 번째 기록", 50);
        Record record2 = new Record(userGroup, "두 번째 기록", 1000);
        recordRepository.save(record1);
        recordRepository.save(record2);

        //When
        List<RecordListResponse> records = recordService.viewRecordList(group.getId(), user.getId());

        //Then
        assertEquals(2, records.size());
        assertEquals("첫 번째 기록", records.get(0).getText());
        assertEquals("두 번째 기록", records.get(1).getText());
    }
}