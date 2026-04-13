package com.project.gym.repository;

import com.project.gym.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, String> {

    List<Booking> findAllByGym_Id(String gymId);

    long countByGym_IdAndBookedDateTime(String gymId, LocalDateTime dateTime);

    default List<Booking> getAllBy(String gymId) {
        return findAllByGym_Id(gymId);
    }

    default int getGymBookingCount(String gymId, LocalDateTime dateTime) {
        return (int) countByGym_IdAndBookedDateTime(gymId, dateTime);
    }

    default Booking getById(String id) {
        return findById(id).orElseThrow();
    }
}
