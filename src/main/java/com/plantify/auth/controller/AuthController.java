package com.plantify.auth.controller;

import com.plantify.auth.domain.dto.response.LoginResponse;
import com.plantify.auth.domain.dto.response.UserResponse;
import com.plantify.auth.global.response.ApiResponse;
import com.plantify.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @RequestParam("code") String authorizationCode) {
        LoginResponse loginResponse = authService.login(authorizationCode);
        return ResponseEntity.ok(ApiResponse.ok(loginResponse));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refreshAccessToken(
            @RequestHeader("Authorization") String authorizationHeader) {
        String accessToken = authService.resolveAccessToken(authorizationHeader);
        String newAccessToken = authService.refreshAccessToken(accessToken);
        return ResponseEntity.ok(ApiResponse.ok(newAccessToken));
    }

    @PostMapping("/validate-token")
    public ResponseEntity<ApiResponse<UserResponse>> getUserInfo(
            @RequestHeader("Authorization") String authorizationHeader) {
        String accessToken = authService.resolveAccessToken(authorizationHeader);
        UserResponse userInfo = authService.getUserIdAndRoleFromToken(accessToken);
        return ResponseEntity.ok(ApiResponse.ok(userInfo));
    }

}
