package com.bookmile.backend.domain.userGroup.repository;

import com.bookmile.backend.domain.userGroup.entity.UserGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    boolean existsByUserIdAndGroupId(Long userId, Long groupId);

    int countByGroupId(Long groupId);

    List<UserGroup> findByGroupId(Long groupId);

    // 순서상 userId, groupId로 검사하여 -> 404 에러가 뜰 경우 있음
    // @Param으로 명서
    Optional<UserGroup> findByUserIdAndGroupId(@Param("userId")Long userId, @Param("groupId")Long groupId);

    @Query("SELECT ug FROM UserGroup ug WHERE ug.group.id = :groupId AND ug.role = 'MASTER'")
    Optional<UserGroup> findMasterByGroupId(@Param("groupId") Long groupId);

    @Query(value = "SELECT user_id FROM user_group WHERE group_id = :groupId ORDER BY RAND()", nativeQuery = true)
    List<Long> findUserRandomSortByGroupId(Long groupId);
}