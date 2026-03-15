package com.project.domain.gym;

import com.project.domain.booking.Booking;
import com.project.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class UserGym {
    @Id
    private final String id;

    @ManyToOne
    private final User user;

    @ManyToOne
    private final Gym gym;

    private final List<UserPass> passes;
    private final List<Booking> bookings;

    public UserGym(User user, Gym gym) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.gym = gym;
        this.passes = new ArrayList<>();
        this.bookings = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Gym getGym() {
        return gym;
    }

    public List<UserPass> getPasses() {
        return passes;
    }

    public List<Booking> getBookings() {
        return bookings;
    }
}