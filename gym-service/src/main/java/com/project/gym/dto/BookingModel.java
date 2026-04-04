package com.project.gym.dto;

import java.time.LocalDateTime;

public record BookingModel(
        String id,
        String userId,
        LocalDateTime dateTime,
        String passId
) {
}
