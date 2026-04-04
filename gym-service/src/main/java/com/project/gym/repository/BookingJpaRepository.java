package com.project.gym.repository;

import com.project.gym.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingJpaRepository extends JpaRepository<Booking, String> {
}
