package com.project.api.user;

import com.project.api.response.BaseResponse;
import com.project.api.response.ResponseService;
import com.project.api.response.exception.ExceptionStatus;
import com.project.api.user.dto.RegisterUserReq;
import com.project.api.user.dto.RegisterUserRes;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/users")
public class UserController {
    private final ResponseService responseService = new ResponseService();

    //유저 회원가입
    @PostMapping("")
    public BaseResponse<RegisterUserRes> registerUser(
            @RequestBody @Valid RegisterUserReq req){
        //req.ticket 에 있는 이메일이랑 req.email 이 같은지 확인한다

        //ticket 에서 이메일이 검증되었는지 확인한다

        //비밀번호가 같은지 확인한다
        var isEqual = req.getPassword().equals(req.getPasswordConfirm());
        if(!isEqual){
            return responseService.getFailureResponse(ExceptionStatus.PASSWORD_NOT_MATCH);
        }
        else
        {
            var res = new RegisterUserRes("jwt", "userId");
            return responseService.getSuccessResponse(res);
        }
    }

    // 자동 로그인
    @GetMapping("/auto-login")
    public BaseResponse<String> autoLogin(
            @RequestHeader(value = "Authorization", required = false) String token
    ){
        //jwt header 없음 -> 회원가입 유도
        if (token == null || !token.startsWith("Bearer ")) {
            return responseService.getFailureResponse(ExceptionStatus.NEED_TO_SIGNUP);
        }
        // jwt 만료 -> 로그인 유도

        // 유효한 jwt -> 로그인 시키고 아이디 가져오기

        return responseService.getSuccessResponse("userId");
    }
}
