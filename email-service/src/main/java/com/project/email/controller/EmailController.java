package com.project.email.controller;

import com.project.common.response.ApiResponse;
import com.project.email.dto.SendEmailVerificationCodeReq;
import com.project.email.dto.VerifyEmailReq;
import com.project.email.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/app/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    //이메일 인증코드 발송
    @PostMapping("/code")
    public CompletableFuture<ApiResponse<Void>> sendEmailVerificationCode(
            @RequestBody @Valid SendEmailVerificationCodeReq req) {

        return emailService.sendVerificationCode(req.getEmail()).thenApply(ApiResponse::success);
    }

    //이메일 인증코드 확인
    @PostMapping("/verification")
    public ApiResponse<String> verifyEmail(
            @RequestBody @Valid VerifyEmailReq req) {
        String ticket = emailService.verifyEmail(req.getEmail(), req.getCode());

        return ApiResponse.success(ticket);
    }
}
