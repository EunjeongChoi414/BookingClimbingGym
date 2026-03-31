package com.project.domain.exception;

public class UnusedPassExistsException extends DomainException {
    public UnusedPassExistsException() {
        super("회수가 남은 패스가 있습니다.");
    }
}
