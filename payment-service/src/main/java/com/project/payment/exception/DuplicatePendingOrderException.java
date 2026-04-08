package com.project.payment.exception;

import com.project.common.exception.DomainException;

public class DuplicatePendingOrderException extends DomainException {
    public DuplicatePendingOrderException() {
        super("이미 진행 중인 결제가 있습니다.");
    }
}
