package com.bookmile.backend.domain.group.repository;

import com.bookmile.backend.domain.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}