package com.bookmile.backend.global.jwt;

import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.user.repository.UserRepository;
import com.bookmile.backend.global.common.StatusCode;
import com.bookmile.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("UserDetailsService.loadUserByUsername: username - {}", email);
        User user =  userRepository.findByEmail(email).orElseThrow(() -> new CustomException(StatusCode.USER_NOT_FOUND));
        log.info("UserDetailsService.loadUserByUsername: username - {}", user.getEmail());
        return new CustomUserDetails(user);
    }
}
