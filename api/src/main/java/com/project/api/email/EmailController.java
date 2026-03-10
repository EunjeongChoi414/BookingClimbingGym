package com.project.api.email;

import com.project.api.email.dto.SendEmailVerificationCodeReq;
import com.project.api.email.dto.VerifyEmailReq;
import com.project.api.response.BaseResponse;
import com.project.api.response.ResponseService;
import com.project.services.email.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/email")
@RequiredArgsConstructor
public class EmailController {

    private final ResponseService responseService = new ResponseService();
    private final EmailService emailService;

    //이메일 인증코드 발송
    @PostMapping("/code")
    public BaseResponse<String> sendEmailVerificationCode(
            @RequestBody @Valid SendEmailVerificationCodeReq req) {

        emailService.sendVerificationEmail(req.getEmail());

        return responseService.getSuccessResponse();
    }

    //이메일 인증코드 확인
    @PostMapping("/verification")
    public BaseResponse<String> verifyEmail (
            @RequestBody @Valid VerifyEmailReq req) {
        String ticket = emailService.verifyEmail(req.getEmail(), String.valueOf(req.getCode()));

        return responseService.getSuccessResponse(ticket);
    }
}
