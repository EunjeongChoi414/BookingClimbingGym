package com.project.services.booking.model;

import java.time.LocalDateTime;

public record BookingDetailModel (
        String id,
        String userId,
        LocalDateTime dateTime,
        String passId
){
}
