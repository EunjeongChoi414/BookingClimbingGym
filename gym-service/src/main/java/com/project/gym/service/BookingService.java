package com.project.gym.service;

import com.project.gym.entity.Booking;
import com.project.gym.repository.BookingRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public void cancelBooking(String userId, String bookingId) {
        Booking booking = bookingRepository.getById(bookingId);
        if (!booking.getUserId().equals(userId)) {
            throw new RuntimeException("올바른 예약이 아닙니다.");
        }

        bookingRepository.delete(booking);
        //유저 패스 다시 채우기.
    }
}
