package com.plantify.auth.service;

import com.plantify.auth.domain.dto.response.KakaoInfoResponse;
import com.plantify.auth.domain.dto.response.LoginResponse;
import com.plantify.auth.domain.dto.response.UserResponse;
import com.plantify.auth.domain.entity.User;

public interface AuthService {

    LoginResponse login(String authorizationCode);
    String refreshAccessToken(String token);
    UserResponse getUserIdAndRoleFromToken(String token);
    Long getUserIdFromToken(String token);
    User findOrCreateMember(KakaoInfoResponse response);
}
