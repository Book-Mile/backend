package com.bookmile.backend.domain.image.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.group.entity.Role;
import com.bookmile.backend.domain.image.dto.req.ImageSaveRequest;
import com.bookmile.backend.domain.image.dto.res.ImageListResponse;
import com.bookmile.backend.domain.image.entity.Image;
import com.bookmile.backend.domain.image.repository.ImageRepository;
import com.bookmile.backend.domain.record.entity.Record;
import com.bookmile.backend.domain.record.repository.RecordRepository;
import com.bookmile.backend.domain.record.service.GroupRepository;
import com.bookmile.backend.domain.record.service.RecordService;
import com.bookmile.backend.domain.record.service.UserGroupRepository;
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
@Transactional
class ImageServiceTest {
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

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageService imageService;

    private Book book;
    private User user;
    private Group group;
    private UserGroup userGroup;
    private Record record;

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

        record = new Record(userGroup, "재미있네", 159);
        recordRepository.save(record);
    }

    @Test
    void 기록에_이미지_저장() {
        //When
        imageService.saveImages(record.getId(), new ImageSaveRequest(List.of("url1", "url2", "url3")));

        List<Image> images = imageRepository.findAllByRecordId(record.getId());

        //Then
        assertEquals(3, images.size());
    }

    @Test
    void 기록_이미지_조회() {
        //When
        imageService.saveImages(record.getId(), new ImageSaveRequest(List.of("url1", "url2", "url3")));
        List<ImageListResponse> imageUrls = imageService.viewImages(record.getId());

        //Then
        assertEquals("url1", imageUrls.get(0).getImageUrls());
        assertEquals("url2", imageUrls.get(1).getImageUrls());
        assertEquals("url3", imageUrls.get(2).getImageUrls());
    }

    @Test
    void 기록_이미지_삭제() {
        //When
        imageService.saveImages(record.getId(), new ImageSaveRequest(List.of("url1", "url2", "url3")));
        List<Image> images = imageRepository.findAllByRecordId(record.getId());

        // 삭제 전
        assertEquals(Boolean.FALSE, images.get(0).getIsDeleted());
        assertEquals(Boolean.FALSE, images.get(1).getIsDeleted());
        assertEquals(Boolean.FALSE, images.get(2).getIsDeleted());

        //Then
        imageService.deleteImage(images.get(1).getId());

        // 삭제 후
        assertEquals(Boolean.FALSE, images.get(0).getIsDeleted());
        assertEquals(Boolean.TRUE, images.get(1).getIsDeleted());
        assertEquals(Boolean.FALSE, images.get(2).getIsDeleted());
    }
}