package com.project.user.controller;

import com.project.common.response.ApiResponse;
import com.project.user.dto.RegisterUserReq;
import com.project.user.dto.RegisterUserRes;
import com.project.user.dto.RegisteredUserInfo;
import com.project.user.service.UserService;
import com.project.common.jwt.JwtRequired;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //유저 회원가입
    @PostMapping("")
    public ApiResponse<RegisterUserRes> registerUser(@RequestBody @Valid RegisterUserReq req) {
        RegisteredUserInfo userInfo = userService.registerUser(
                req.getEmail(), req.getPassword(), req.getPasswordConfirm());
        var res = new RegisterUserRes(userInfo.jwt(), userInfo.userId());

        return ApiResponse.success(res);
    }

    // 자동 로그인
    @JwtRequired
    @GetMapping("/auto-login")
    public ApiResponse<String> autoLogin(
            @RequestAttribute(name = "userId") String userId
    ) {
        String loggedInUserId = userService.loginUser(userId);

        return ApiResponse.success(loggedInUserId);
    }
}
