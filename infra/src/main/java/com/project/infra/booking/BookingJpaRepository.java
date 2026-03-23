package com.project.infra.booking;

import com.project.domain.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingJpaRepository extends JpaRepository<Booking, String> {
}
