package com.project.api.user;

import com.project.api.response.BaseResponse;
import com.project.api.response.ResponseService;
import com.project.api.response.exception.ExceptionStatus;
import com.project.api.user.dto.RegisterUserReq;
import com.project.api.user.dto.RegisterUserRes;
import com.project.services.email.EmailService;
import com.project.services.user.UserService;
import com.project.services.user.model.RegisteredUserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/users")
@RequiredArgsConstructor
public class UserController {
    private final ResponseService responseService = new ResponseService();
    private final EmailService emailService;
    private final UserService userService;

    //유저 회원가입
    @PostMapping("")
    public BaseResponse<RegisterUserRes> registerUser(@RequestBody @Valid RegisterUserReq req){
        emailService.checkIfEmailVerified(req.getTicket(), req.getEmail());

        var isEqual = req.getPassword().equals(req.getPasswordConfirm());
        if(!isEqual){
            return responseService.getFailureResponse(ExceptionStatus.PASSWORD_NOT_MATCH);
        }
        else
        {
            RegisteredUserInfo userInfo = userService.registerUser(req.getEmail(), req.getPassword());
            var res = new RegisterUserRes(userInfo.jwt(), userInfo.userId());
            return responseService.getSuccessResponse(res);
        }
    }

    // 자동 로그인
    @GetMapping("/auto-login")
    public BaseResponse<String> autoLogin(
            @RequestHeader(value = "Authorization", required = false) String token
    ){
        if (token == null || !token.startsWith("Bearer ")) {
            return responseService.getFailureResponse(ExceptionStatus.NEED_TO_SIGNUP);
        }

        String userId = userService.tryLoginUser(token.substring("Bearer ".length()));

        return responseService.getSuccessResponse(userId);
    }
}
