package com.bookmile.backend.domain.userGroup.repository;

import com.bookmile.backend.domain.userGroup.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    boolean existsByUserIdAndGroupId(Long userId, Long groupId);
    int countByGroupId(Long groupId);
    List<UserGroup> findByGroupId(Long groupId);

    Optional<UserGroup> findByUserIdAndGroupId(Long userId, Long groupId);

    @Query("SELECT u.user.nickname FROM UserGroup u WHERE u.group.id = :groupId AND u.role = 'MASTER'")
    Optional<String> findMasterNicknameByGroupId(@Param("groupId") Long groupId);

    @Query("SELECT ug FROM UserGroup ug WHERE ug.group.id = :groupId AND ug.role = 'MASTER'")
    Optional<UserGroup> findMasterByGroupId(@Param("groupId") Long groupId);
}