package com.project.infra.booking;

import com.project.domain.booking.Booking;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BookingRepository implements com.project.domain.booking.BookingRepository {
    private final Map<String, Booking> storage = new ConcurrentHashMap<>();

    @Override
    public void add(Booking booking) {
        storage.put(booking.getId(), booking);
    }

    @Override
    public List<Booking> getAllBy(String gymId) {
        return storage.values().stream()
                .filter(booking -> booking.getGym().getId().equals(gymId))
                .toList();
    }

    @Override
    public int getGymBookingCount(String gymId, LocalDateTime dateTime) {
        long count = storage.values().stream()
                .filter(booking -> booking.getGym().getId().equals(gymId)
                        && booking.getBookedDateTime().isEqual(dateTime))
                .count();
        return (int) count;
    }

    @Override
    public void delete(Booking booking) {
        storage.remove(booking.getId());
    }

    @Override
    public Booking findById(String id) {
        return storage.get(id);
    }
}
