package com.project.domain.booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository {
    void add(Booking booking);
    List<Booking> getAllBy(String gymId);
    int getBookingCount(String gymId, LocalDateTime dateTime);
    void delete(Booking booking);
    Booking findById(String id);
}
