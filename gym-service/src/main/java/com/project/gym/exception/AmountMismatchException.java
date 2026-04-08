package com.project.gym.exception;

import com.project.common.exception.DomainException;

public class AmountMismatchException extends DomainException {
    public AmountMismatchException() {
        super("결제 금액이 일치하지 않습니다.");
    }
}
