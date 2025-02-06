package com.bookmile.backend.domain.record.repository;

import com.bookmile.backend.domain.record.entity.Record;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findAllByUserGroupId(Long userGroupId);

    @Query(value = "SELECT * FROM record  WHERE group_id = :groupId ORDER BY RAND() LIMIT 4", nativeQuery = true)
    List<Record> findRandomRecordByGroupId(Long groupId);

}
