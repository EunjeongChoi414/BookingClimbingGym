package com.project.gym.exception;

import com.project.common.exception.DomainException;

public class AlreadyProcessedException extends DomainException {
    public AlreadyProcessedException() {
        super("이미 처리된 주문입니다.");
    }
}
