package com.bookmile.backend.domain.user.repository;

import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.user.entity.UserOAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserOAuthRepository extends JpaRepository<UserOAuth, Long> {
    Optional<UserOAuth> findByProviderAndProviderId(String provider, String providerId);
    List<UserOAuth> findByUserId(Long userId);
    Optional<UserOAuth> findByUserIdAndProvider(Long userId, String provider);
}
