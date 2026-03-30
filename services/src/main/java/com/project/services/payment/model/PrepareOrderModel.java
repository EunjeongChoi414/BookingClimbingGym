package com.project.services.payment.model;

import java.math.BigDecimal;

public record PrepareOrderModel(
        String orderId,
        BigDecimal amount,
        String passName
) {
}
