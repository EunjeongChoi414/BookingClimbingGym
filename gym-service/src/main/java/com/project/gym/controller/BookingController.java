package com.project.gym.controller;

import com.project.common.jwt.JwtRequired;
import com.project.common.response.ApiResponse;
import com.project.gym.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @JwtRequired
    @DeleteMapping("/{bookingId}")
    public ApiResponse<Void> cancelBooking(
            @RequestAttribute(name = "userId") String userId,
            @PathVariable String bookingId
    ) {
        bookingService.cancelBooking(userId, bookingId);
        return ApiResponse.success();
    }
}