package com.project.services.gym.model;

import java.math.BigDecimal;

public record PrepareOrderModel(
        String orderId,
        BigDecimal amount,
        String passName
) {}
