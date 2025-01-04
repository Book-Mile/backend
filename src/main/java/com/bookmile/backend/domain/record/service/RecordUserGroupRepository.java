package com.bookmile.backend.domain.record.service;

import com.bookmile.backend.domain.userGroup.entity.UserGroup;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecordUserGroupRepository extends JpaRepository<UserGroup, Long> {

    @Query(value = "SELECT usergroup_id FROM user_group WHERE group_id = :groupId AND user_id = :userId", nativeQuery = true)
    Optional<Long> findUserGroupIdByGroupIdAndUserId(Long groupId, Long userId);

    UserGroup findUserGroupById(Long userGroupId);
}