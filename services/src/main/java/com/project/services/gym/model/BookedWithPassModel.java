package com.project.services.gym.model;

public record BookedWithPassModel(
        String bookingId,
        int remainingUses,
        String qrToken
) {
}
