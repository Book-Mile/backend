package com.bookmile.backend.domain.user.service;

import com.bookmile.backend.domain.user.dto.res.UserInfoDto;
import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.user.repository.UserRepository;
import com.bookmile.backend.domain.user.service.impl.UserServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class UserServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Test
    void 사용자_정보_조회() {
        User user = new User(null, "kjy154969@gmail.com", "1234", null);

        userRepository.save(user);

        UserInfoDto userInfo = userServiceImpl.getUserInfo(user.getId());

        Assertions.assertEquals(null, userInfo.getNickName());
        Assertions.assertEquals("kjy154969@gmail.com", userInfo.getEmail());
        Assertions.assertEquals(null, userInfo.getImage());

    }

}
