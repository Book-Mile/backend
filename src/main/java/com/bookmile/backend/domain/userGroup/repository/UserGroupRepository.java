package com.bookmile.backend.domain.userGroup.repository;

import com.bookmile.backend.domain.userGroup.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
}
