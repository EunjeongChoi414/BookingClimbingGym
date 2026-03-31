package com.project.services.gym.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConfirmedPassModel(
        String userPassId,
        String passName,
        LocalDate validUntil,
        int remainingUses
) {}
