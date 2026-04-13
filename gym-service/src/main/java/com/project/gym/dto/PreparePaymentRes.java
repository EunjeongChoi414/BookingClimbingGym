package com.project.gym.dto;

import java.math.BigDecimal;

public record PreparePaymentRes(
        String orderId,
        BigDecimal amount,
        String passName
) {
}
