package com.project.common.exception;

import com.project.common.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleArgumentException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        return ApiResponse.fail(ErrorCode.INVALID_REQUEST.getCode(), message);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ApiResponse<Void> handleExpiredJwtException(ExpiredJwtException e) {
        return ApiResponse.fail(ErrorCode.EXPIRED_TOKEN);
    }

    @ExceptionHandler(DomainException.class)
    public ApiResponse<Void> handleDomainException(DomainException e) {
        return ApiResponse.fail(e.getErrorCode());
    }
}
