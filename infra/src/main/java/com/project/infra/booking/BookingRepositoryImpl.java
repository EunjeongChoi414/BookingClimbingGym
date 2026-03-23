package com.project.infra.booking;

import com.project.domain.booking.Booking;
import com.project.domain.booking.BookingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class BookingRepositoryImpl implements BookingRepository {

    private final BookingJpaRepository jpaRepository;

    public BookingRepositoryImpl(BookingJpaRepository bookingJpaRepository) {
        this.jpaRepository = bookingJpaRepository;
    }

    @Override
    public void add(Booking booking) {
        jpaRepository.save(booking);
    }

    @Override
    public List<Booking> getAllBy(String gymId) {
        return List.of();
    }

    @Override
    public int getGymBookingCount(String gymId, LocalDateTime dateTime) {
        return 0;
    }

    @Override
    public void delete(Booking booking) {
        jpaRepository.delete(booking);
    }

    @Override
    public Booking getById(String id) {
        return jpaRepository.findById(id).get();
    }
}
