package com.project.services.booking;

import com.project.domain.booking.Booking;
import com.project.domain.booking.BookingRepository;
import com.project.domain.exception.DomainException;
import com.project.domain.exception.ErrorCode;
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
            throw new DomainException(ErrorCode.INVALID_BOOKING);
        }

        bookingRepository.delete(booking);
        //유저 패스 다시 채우기.
    }
}
