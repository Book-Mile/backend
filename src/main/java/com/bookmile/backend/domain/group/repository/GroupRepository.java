package com.bookmile.backend.domain.group.repository;

import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.group.entity.GroupStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("SELECT g FROM Group g WHERE g.book.isbn13 = :isbn13 AND g.status = :status")
    List<Group> findByIsbn13AndStatusAndIsOpenTrue(@Param("isbn13") String isbn13, @Param("status") GroupStatus status);
}