package com.plantify.auth.global.exception;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;

    public ApplicationException(ErrorCode errorCode) {
        httpStatus = errorCode.getHttpStatus();
        message = errorCode.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return null;
    }
}