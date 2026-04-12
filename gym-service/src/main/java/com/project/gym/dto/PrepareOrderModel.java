package com.project.gym.dto;

import java.math.BigDecimal;

public record PrepareOrderModel(
        String orderId,
        BigDecimal amount,
        String passName
) {
}
