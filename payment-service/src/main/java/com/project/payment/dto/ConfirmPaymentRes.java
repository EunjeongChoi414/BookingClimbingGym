package com.project.payment.dto;

import java.time.LocalDate;

public record ConfirmPaymentRes(
        String userPassId,
        String passName,
        LocalDate validUntil,
        int remainingUses
) {
}
