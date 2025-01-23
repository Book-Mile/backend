package com.bookmile.backend.domain.user.service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface OAuthService {
    List<String> getOAuthProviders(String email);
    void unlinkUserOAuth(HttpServletRequest request, String provider, String email);

    Map<String, String> testSocialLogin(String email);
}
