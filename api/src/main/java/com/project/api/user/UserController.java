package com.project.api.user;

import com.project.api.response.ApiResponse;
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
    private final EmailService emailService;
    private final UserService userService;

    //유저 회원가입
    @PostMapping("")
    public ApiResponse<RegisterUserRes> registerUser(@RequestBody @Valid RegisterUserReq req) {
        emailService.checkIfEmailVerified(req.getTicket(), req.getEmail());

        RegisteredUserInfo userInfo = userService.registerUser(
                req.getEmail(), req.getPassword(), req.getPasswordConfirm());
        var res = new RegisterUserRes(userInfo.jwt(), userInfo.userId());

        return ApiResponse.success(res);
    }

    // 자동 로그인
    @GetMapping("/auto-login")
    public ApiResponse<String> autoLogin(
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        String userId = userService.loginUser(token == null ? null : token.substring("Bearer ".length()));

        return ApiResponse.success(userId);
    }
}
