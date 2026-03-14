package com.project.domain.gym;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class UserBooking {
    private final String id;
    private final LocalDateTime startDateTime;

    public UserBooking(LocalDateTime startDateTime) {
        this.id = UUID.randomUUID().toString();
        this.startDateTime = startDateTime;
    }

    public String getId() { return id; }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }
}