package com.project.gym.dto;

import java.time.LocalDate;

public record ConfirmedPassModel(
        String userPassId,
        String passName,
        LocalDate validUntil,
        int remainingUses
) {
}
