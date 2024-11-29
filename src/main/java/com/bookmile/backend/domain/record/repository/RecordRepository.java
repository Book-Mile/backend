package com.bookmile.backend.domain.record.repository;

import com.bookmile.backend.domain.record.entity.Record;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecordRepository extends JpaRepository<Record, Long> {
    @Query(value = "SELECT * FROM record WHERE usergroup_id = :userGroupId", nativeQuery = true)
    List<Record> findAllByUserGroupId(Long userGroupId);
}
