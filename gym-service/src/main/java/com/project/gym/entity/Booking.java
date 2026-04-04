package com.project.gym.entity;

import com.project.gym.exception.BookingCannotBeCancelledException;
import com.project.gym.exception.InvalidBookingException;
import com.project.gym.entity.Gym;
import com.project.gym.entity.UserPass;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Booking {
    @Id
    private String id;
    private String userId;

    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym gym;

    private String passId;
    private LocalDateTime createdAt;
    private LocalDateTime bookedDateTime;
    private String qrToken;

    protected Booking() {
    }

    public Booking(
            String userId, Gym gym, String passId, LocalDateTime bookedDateTime,
            UserPass userPass, Clock clock) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.gym = gym;
        this.passId = passId;
        this.createdAt = LocalDateTime.now();
        this.bookedDateTime = bookedDateTime;
        this.qrToken = UUID.randomUUID().toString();
        userPass.usePass(clock);
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

    public void cancel(LocalDateTime now, int cancellationNoticeDays) {
        isValid(now);
        if (!bookedDateTime.minusDays(cancellationNoticeDays).isAfter(now)) {
            throw new BookingCannotBeCancelledException();
        }
    }

    private void isValid(LocalDateTime now) {
        if (!now.isBefore(bookedDateTime)) {
            throw new InvalidBookingException();
        }
    }
}