package com.bookmile.backend.domain.record.repository;

import com.bookmile.backend.domain.record.entity.Record;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findAllByUserGroupId(Long userGroupId);

    @Query(value = "SELECT r.* FROM record r " +
            "JOIN user_group ug ON r.usergroup_id = ug.usergroup_id " +
            "WHERE ug.group_id = :groupId " +
            "ORDER BY RAND() LIMIT 4", nativeQuery = true)
    List<Record> findRandomRecordByGroupId(@Param("groupId") Long groupId);

    @Query(value = "SELECT r.* FROM record r " +
            "WHERE r.usergroup_id = :userGroupId " +
            "AND r.created_at = ( " +
            "    SELECT MAX(r2.created_at) FROM record r2 " +
            "    WHERE r2.usergroup_id = r.usergroup_id " +
            ")",
            nativeQuery = true)
    Optional<Record> findLatestRecordByUserAndGroup(@Param("userGroupId") Long userGroupId);
}