package com.bookmile.backend.domain.record.service;

import com.bookmile.backend.domain.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}