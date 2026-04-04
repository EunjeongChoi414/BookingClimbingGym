package com.project.gym.repository;

import com.project.gym.entity.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository {
    void add(Booking booking);

    List<Booking> getAllBy(String gymId);

    int getGymBookingCount(String gymId, LocalDateTime dateTime);

    void delete(Booking booking);

    Booking getById(String id);
}
