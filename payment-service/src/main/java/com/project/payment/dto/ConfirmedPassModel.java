package com.project.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConfirmedPassModel(
        String userPassId,
        String passName,
        LocalDate validUntil,
        int remainingUses
) {}
