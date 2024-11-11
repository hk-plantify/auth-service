package com.plantify.auth.global.exception;

import com.plantify.auth.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiResponse<Void>> handleApplicationException(ApplicationException e) {
        HttpStatus status = e.getHttpStatus();
        ApiResponse<Void> response = ApiResponse.fail(status, e.getMessage());
        return ResponseEntity.status(status).body(response);
    }
}
