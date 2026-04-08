package com.project.payment.dto;

import java.math.BigDecimal;

public record PrepareOrderModel(
        String orderId,
        BigDecimal amount,
        String passName
) {
}
