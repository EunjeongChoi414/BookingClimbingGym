package com.project.common.exception;

import com.project.common.response.BaseResponse;
import com.project.common.response.ResponseService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final ResponseService responseService = new ResponseService();


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<String> handleArgumentException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return responseService.getArgumentFailedResponse(message);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public BaseResponse<String> handleExpiredJwtException(ExpiredJwtException e) {
        return responseService.getFailureResponse(ExceptionStatus.EXPIRED_TOKEN);
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<String> handleDomainException(RuntimeException e) {
        return responseService.getFailureResponse(e.getMessage());
    }
}
