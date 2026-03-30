package com.project.api.gym.dto;

import java.math.BigDecimal;

public record PreparePurchaseRes(
        String orderId,
        BigDecimal amount,
        String passName
) {}
