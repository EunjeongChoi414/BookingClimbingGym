package com.project.gym.port;

import com.project.gym.entity.PaymentConfirmResult;

import java.math.BigDecimal;

public interface PaymentClient {
    PaymentConfirmResult confirm(String paymentKey, String orderId, BigDecimal amount);
}
