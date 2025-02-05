package com.bookmile.backend.domain.user.repository;

import com.bookmile.backend.domain.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long userId);

    Boolean existsByNickname(String nickname);

    Boolean existsByEmailAndIsDeletedFalse(String email);

    @Query(value = "SELECT * FROM user WHERE groupId = :groupId ORDER BY RAND()", nativeQuery = true)
    List<User> findUserRandomSortByGroupId(@Param("groupId") Long groupId);
}

