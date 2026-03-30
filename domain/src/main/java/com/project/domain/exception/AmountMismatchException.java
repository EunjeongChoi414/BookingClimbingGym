package com.project.domain.exception;

public class AmountMismatchException extends DomainException {
    public AmountMismatchException() {
        super("결제 금액이 일치하지 않습니다.");
    }
}
