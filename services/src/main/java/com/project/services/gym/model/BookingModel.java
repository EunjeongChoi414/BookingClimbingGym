package com.project.services.gym.model;

import java.time.LocalDateTime;

public record BookingModel(
        String id,
        String userId,
        LocalDateTime dateTime,
        String passId
) {
}
