package com.project.api.response.exception;

import com.project.api.response.ApiResponse;
import com.project.domain.exception.DomainException;
import com.project.domain.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleArgumentException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
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
