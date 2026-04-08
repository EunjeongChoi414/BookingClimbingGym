package com.project.payment.exception;

import com.project.common.exception.DomainException;

public class PaymentFailedException extends DomainException {
    public PaymentFailedException(String reason) {
        super("결제에 실패했습니다: " + reason);
    }
}
