package com.bookmile.backend.domain.user.repository;

import com.bookmile.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<User> findById(Long userId);
    Boolean existsByNickname(String nickname);
}

