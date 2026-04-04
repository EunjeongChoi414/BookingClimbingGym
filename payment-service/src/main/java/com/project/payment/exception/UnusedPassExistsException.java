package com.project.payment.exception;

import com.project.common.exception.DomainException;

public class UnusedPassExistsException extends DomainException {
    public UnusedPassExistsException() {
        super("회수가 남은 패스가 있습니다.");
    }
}
