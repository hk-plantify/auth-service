package com.plantify.auth.service;

import com.plantify.auth.controller.client.KakaoApiClient;
import com.plantify.auth.domain.dto.response.KakaoInfoResponse;
import com.plantify.auth.domain.dto.response.LoginResponse;
import com.plantify.auth.domain.dto.response.KakaoTokenResponse;
import com.plantify.auth.domain.dto.response.UserResponse;
import com.plantify.auth.domain.entity.Role;
import com.plantify.auth.domain.entity.User;
import com.plantify.auth.global.exception.ApplicationException;
import com.plantify.auth.global.exception.errorcode.AuthErrorCode;
import com.plantify.auth.global.exception.errorcode.UserErrorCode;
import com.plantify.auth.jwt.JwtAuthProvider;
import com.plantify.auth.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtAuthProvider jwtAuthProvider;
    private final KakaoApiClient kakaoApiClient;
    private final UserRepository userRepository;

    @Override
    public LoginResponse login(String authorizationCode) {
        KakaoTokenResponse tokenResponse = kakaoApiClient.requestAccessToken(authorizationCode);
        KakaoInfoResponse infoResponse = kakaoApiClient.requestKakaoUserInfo(tokenResponse.accessToken());

        User user = findOrCreateMember(infoResponse);
        String accessToken = jwtAuthProvider.createAccessToken(user.getKakaoId());
        String refreshToken = jwtAuthProvider.createRefreshToken(user.getKakaoId());

        return LoginResponse.from(user, accessToken, refreshToken);
    }

    @Override
    public String refreshAccessToken(String token) {
        Long kakaoId = getUserIdFromToken(token);
        return jwtAuthProvider.createAccessToken(kakaoId);
    }

    @Override
    public UserResponse getUserIdAndRoleFromToken(String token) {
        Long kakaoId = getUserIdFromToken(token);
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new ApplicationException(UserErrorCode.USER_NOT_FOUND));

        return UserResponse.from(user);
    }

    @Override
    public Long getUserIdFromToken(String token) {
        if (token == null || !jwtAuthProvider.validateToken(token)) {
            throw new ApplicationException(AuthErrorCode.INVALID_TOKEN);
        }
        return jwtAuthProvider.getClaims(token).get("kakaoId", Long.class);
    }

    @Override
    public User findOrCreateMember(KakaoInfoResponse response) {
        String username = response.getUsername();
        if (username == null || username.isEmpty()) {
            throw new ApplicationException(UserErrorCode.INVALID_USERNAME);
        }

        return userRepository.findByKakaoId(response.id())
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .kakaoId(response.id())
                            .username(username)
                            .role(Role.USER)
                            .build();
                    return userRepository.save(newUser);
                });
    }
}