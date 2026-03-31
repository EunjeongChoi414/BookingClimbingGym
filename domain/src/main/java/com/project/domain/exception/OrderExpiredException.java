package com.project.domain.exception;

public class OrderExpiredException extends DomainException {
    public OrderExpiredException() {
        super("주문이 만료되었습니다. 다시 시도해주세요.");
    }
}
