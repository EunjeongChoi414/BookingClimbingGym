package com.project.gym.exception;

import com.project.common.exception.DomainException;

public class OrderNotFoundException extends DomainException {
    public OrderNotFoundException() {
        super("주문을 찾을 수 없습니다.");
    }
}
