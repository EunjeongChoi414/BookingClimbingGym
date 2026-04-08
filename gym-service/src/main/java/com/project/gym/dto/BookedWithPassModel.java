package com.project.gym.dto;

public record BookedWithPassModel(
        String bookingId,
        int remainingUses,
        String qrToken
) {
}
