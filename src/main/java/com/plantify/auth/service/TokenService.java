package com.plantify.auth.service;

import com.plantify.auth.domain.dto.response.KakaoInfoResponse;
import com.plantify.auth.domain.entity.User;

public interface TokenService {

    String resolveAccessToken(String authorizationHeader);
    Long getUserIdFromToken(String token);
    User findOrCreateMember(KakaoInfoResponse response);
}
