package com.project.domain.exception;

public class AlreadyProcessedException extends DomainException {
    public AlreadyProcessedException() {
        super("이미 처리된 주문입니다.");
    }
}
