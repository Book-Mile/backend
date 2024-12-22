package com.bookmile.backend.domain.record.repository;

import com.bookmile.backend.domain.record.entity.Record;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findAllByUserGroupId(Long userGroupId);
}
