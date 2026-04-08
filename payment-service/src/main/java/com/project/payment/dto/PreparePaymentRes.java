package com.project.payment.dto;

import java.math.BigDecimal;

public record PreparePaymentRes(
        String orderId,
        BigDecimal amount,
        String passName
) {
}
