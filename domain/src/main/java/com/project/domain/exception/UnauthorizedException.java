package com.project.domain.exception;

public class UnauthorizedException extends DomainException {
    public UnauthorizedException() {
        super("권한이 없습니다.");
    }
}
