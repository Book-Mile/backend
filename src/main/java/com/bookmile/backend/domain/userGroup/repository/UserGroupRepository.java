package com.bookmile.backend.domain.userGroup.repository;

import com.bookmile.backend.domain.userGroup.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    // 특정 사용자와 그룹 간의 참여 여부 확인
    boolean existsByUserIdAndGroupId(Long userId, Long groupId);
    // 특정 그룹의 현재 참여 인원 수
    int countByGroupId(Long groupId);
}
