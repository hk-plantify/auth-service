package com.plantify.auth.global.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private HttpStatus status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(HttpStatus.OK, "성공", null);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(HttpStatus.OK, "성공", data);
    }

    public static <T> ApiResponse<T> fail(HttpStatus status, String message) {
        return new ApiResponse<>(status, message, null);
    }
}