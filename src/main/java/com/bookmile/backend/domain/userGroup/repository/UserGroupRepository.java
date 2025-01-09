package com.bookmile.backend.domain.userGroup.repository;

import com.bookmile.backend.domain.userGroup.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    boolean existsByUserIdAndGroupId(Long userId, Long groupId);
    int countByGroupId(Long groupId);
    List<UserGroup> findByGroupId(Long groupId);

    Optional<UserGroup> findByUserIdAndGroupId(Long userId, Long groupId);
}