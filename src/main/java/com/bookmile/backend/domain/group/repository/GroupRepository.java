package com.bookmile.backend.domain.group.repository;

import com.bookmile.backend.domain.group.entity.Group;
import com.bookmile.backend.domain.group.entity.GroupStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByIdAndStatus(Long groupId, GroupStatus status);

    @Query("SELECT g FROM Group g WHERE g.book.isbn13 = :isbn13 AND g.status = :status")
    List<Group> findByIsbn13AndStatus(@Param("isbn13") String isbn13, @Param("status") GroupStatus status);
}