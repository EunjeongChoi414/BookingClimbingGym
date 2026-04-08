package com.project.common.exception;

public class UnauthorizedException extends DomainException {
    public UnauthorizedException() {
        super("권한이 없습니다.");
    }
}
