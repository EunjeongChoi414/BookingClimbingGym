package com.project.domain.exception;

public class DuplicatePendingOrderException extends DomainException {
    public DuplicatePendingOrderException() {
        super("이미 진행 중인 결제가 있습니다.");
    }
}
