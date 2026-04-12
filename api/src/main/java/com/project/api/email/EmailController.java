package com.project.api.email;

import com.project.api.email.dto.SendEmailVerificationCodeReq;
import com.project.api.email.dto.VerifyEmailReq;
import com.project.api.response.ApiResponse;
import com.project.services.email.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    //이메일 인증코드 발송
    @PostMapping("/code")
    public ApiResponse<Void> sendEmailVerificationCode(
            @RequestBody @Valid SendEmailVerificationCodeReq req) {

        emailService.sendVerificationCode(req.getEmail());

        return ApiResponse.success();
    }

    //이메일 인증코드 확인
    @PostMapping("/verification")
    public ApiResponse<String> verifyEmail(
            @RequestBody @Valid VerifyEmailReq req) {
        String ticket = emailService.verifyEmail(req.getEmail(), req.getCode());

        return ApiResponse.success(ticket);
    }
}
