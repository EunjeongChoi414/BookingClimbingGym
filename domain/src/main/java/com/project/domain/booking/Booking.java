package com.project.domain.booking;

import com.project.domain.gym.Gym;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Booking {
    private final static int CANCELLABLE_THRESHOLD_DAYS = 1;
    private final String id;
    private final String userId;
    @ManyToOne
    @JoinColumn(name = "gym_id")
    private final Gym gym;
    private final String passId;
    private final LocalDateTime createdAt;
    private final LocalDateTime bookedDateTime;
    private String qrToken;

    public Booking(String userId, Gym gym, String passId, LocalDateTime bookedDateTime) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.gym = gym;
        this.passId = passId;
        this.createdAt = LocalDateTime.now();
        this.bookedDateTime = bookedDateTime;
        this.qrToken = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public Gym getGym() {
        return gym;
    }

    public String getPassId() {
        return passId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getBookedDateTime() {
        return bookedDateTime;
    }

    public String getQrToken() {
        return qrToken;
    }

    public void cancel(LocalDateTime now) {
        isValid(now);
        if (!bookedDateTime.minusDays(CANCELLABLE_THRESHOLD_DAYS).isAfter(now)) {
            throw new RuntimeException("Booking cannot be cancelled");
        }
    }

    private void isValid(LocalDateTime now) {
        if (!now.isBefore(bookedDateTime)) {
            throw new RuntimeException("invalid booking");
        }
    }
}
