package com.project.api.response.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionStatus {
    // email
    INVALID_EMAIL_CODE_VERIFICATION(false, 2000, "코드가 일치하지 않습니다"),
    NEED_TO_SIGNUP(false, 2001, "회원가입이 필요합니다."),
    NEED_TO_LOGIN(false, 2002, "로그인이 필요합니다."),
    EXPIRED_TOKEN(false, 2006, "토큰이 만료되었습니다."),

    //user
    PASSWORD_NOT_MATCH(false, 2003, "비밀번호가 일치하지 않습니다."),

    //gym
    INVALID_BUSINESS_HOURS(false, 2004, "영업시간이 올바르지 않습니다."),
    INVALID_HOURS(false, 2005, "선택한 시간이 올바르지 않습니다.");

    //4000 request argument 오류

    private final boolean isSuccess;
    private final int code;
    private final String message;
}
