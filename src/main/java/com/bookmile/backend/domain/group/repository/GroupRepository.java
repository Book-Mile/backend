package com.bookmile.backend.domain.group.repository;

import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.group.entity.GroupStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByIdAndStatus(Long groupId, GroupStatus status);
}