package com.project.domain.email;

import java.util.UUID;

public class EmailVerification {
    public static String getVerificationCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}
