package com.project.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 2000: signup / login / auth
    INVALID_EMAIL_CODE(2000, "코드가 일치하지 않습니다."),
    NEED_EMAIL_VERIFICATION(2001, "이메일 인증이 필요합니다."),
    NEED_TO_SIGNUP(2002, "회원가입이 필요합니다."),
    NEED_TO_LOGIN_AGAIN(2003, "다시 로그인이 필요합니다."),
    PASSWORD_NOT_MATCH(2004, "비밀번호가 일치하지 않습니다."),
    EXPIRED_TOKEN(2005, "토큰이 만료되었습니다."),
    UNAUTHORIZED(2006, "권한이 없습니다."),

    // 3000: gym
    INVALID_BUSINESS_HOURS(3000, "영업시간이 올바르지 않습니다."),
    INVALID_BOOKING_TIME(3001, "선택한 시간이 올바르지 않습니다."),
    INVALID_BOOKING(3002, "올바른 예약이 아닙니다."),
    BOOKING_CANNOT_BE_CANCELLED(3003, "예약을 취소할 수 없습니다."),
    INVALID_PASS(3004, "올바른 패스가 아닙니다."),
    NO_REMAINING_USES(3005, "패스 이용 횟수가 남아있지 않습니다."),

    // 4000: 요청 매개변수 오류
    INVALID_REQUEST(4000, "입력값을 확인해주세요."),

    // 5000: payment / order
    DUPLICATE_PENDING_ORDER(5000, "이미 진행 중인 결제가 있습니다."),
    UNUSED_PASS_EXISTS(5001, "회수가 남은 패스가 있습니다."),
    ORDER_NOT_FOUND(5002, "주문을 찾을 수 없습니다."),
    ORDER_EXPIRED(5003, "주문이 만료되었습니다. 다시 시도해주세요."),
    AMOUNT_MISMATCH(5004, "결제 금액이 일치하지 않습니다."),
    ALREADY_PROCESSED(5005, "이미 처리된 주문입니다."),
    PAYMENT_FAILED(5006, "결제에 실패했습니다.");

    private final int code;
    private final String message;
}
