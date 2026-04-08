package com.project.common.exception;

public class NeedEmailVerificationException extends DomainException {
    public NeedEmailVerificationException() {
        super("이메일 인증이 필요합니다.");
    }
}
