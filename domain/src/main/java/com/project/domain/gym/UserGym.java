package com.project.domain.gym;

import com.project.domain.booking.Booking;
import com.project.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class UserGym {
    @Id
    private String id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Gym gym;

    @Transient
    private List<UserPass> passes;

    @Transient
    private List<Booking> bookings;

    protected UserGym() {
    }

    public UserGym(User user, Gym gym) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.gym = gym;
        this.passes = new ArrayList<>();
        this.bookings = new ArrayList<>();
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
