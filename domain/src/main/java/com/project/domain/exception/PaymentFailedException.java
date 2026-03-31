package com.project.domain.exception;

public class PaymentFailedException extends DomainException {
    public PaymentFailedException(String reason) {
        super("결제에 실패했습니다: " + reason);
    }
}
