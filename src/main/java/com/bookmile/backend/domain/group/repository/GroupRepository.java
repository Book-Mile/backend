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

    // 모집 중인 그룹 중 가장 최근에 생성된 4개 그룹
    @Query("SELECT g FROM Group g WHERE g.book.isbn13 = :isbn13 AND g.status = :status AND g.isOpen = true ORDER BY g.createdAt DESC LIMIT 4")
    List<Group> findTop4RecentGroupsByIsbn13AndStatus(@Param("isbn13") String isbn13, @Param("status") GroupStatus status);

    // 진행 중,완료된 그룹 중 무작위로 4개 그룹
    @Query("SELECT g FROM Group g JOIN g.book b WHERE b.isbn13 = :isbn13 AND g.status = :status AND g.isOpen = true ORDER BY FUNCTION('RAND') LIMIT 4")
    List<Group> findRandom4GroupsByIsbn13AndStatus(@Param("isbn13") String isbn13, @Param("status") GroupStatus status);
}