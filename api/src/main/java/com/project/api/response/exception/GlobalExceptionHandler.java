package com.project.api.response.exception;

import com.project.api.response.BaseResponse;
import com.project.api.response.ResponseService;
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
}
