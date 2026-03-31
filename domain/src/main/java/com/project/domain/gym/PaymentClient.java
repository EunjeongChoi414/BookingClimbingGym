package com.project.domain.gym;

import java.math.BigDecimal;

public interface PaymentClient {
    PaymentConfirmResult confirm(String paymentKey, String orderId, BigDecimal amount);
}
