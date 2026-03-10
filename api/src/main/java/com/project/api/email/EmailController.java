package com.project.api.email;

import com.project.api.email.dto.SendEmailVerificationCodeReq;
import com.project.api.email.dto.VerifyEmailReq;
import com.project.api.response.BaseResponse;
import com.project.api.response.ResponseService;
import jakarta.validation.Valid;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/email")
public class EmailController {

    private final ResponseService responseService = new ResponseService();

    //이메일 인증코드 발송
    @PostMapping("/code")
    public BaseResponse<String> sendEmailVerificationCode(
            @RequestBody @Valid SendEmailVerificationCodeReq req) {
        //이메일로 코드 전송

        //이메일과 코드 저장

        return responseService.getSuccessResponse();
    }

    //이메일 인증코드 확인
    @PostMapping("/verification")
    public BaseResponse<String> verifyEmail (
            @RequestBody @Valid VerifyEmailReq req) {
        //req의 이메일&코드가 저장되어있는 이메일&코드와 같은지 확인한다. 다르면 예외처리.

        //같으면 인증을 통과했음을 보여주는 토큰인 ticket 을 반환한다.-> 나중에 회원가입할 때 이 티켓으로 이메일이 인증되었는지 확인한다.
        String ticket = UUID.randomUUID().toString();

        return responseService.getSuccessResponse(ticket);
    }
}
