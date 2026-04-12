package com.project.api.booking;

import com.project.api.response.ApiResponse;
import com.project.services.booking.BookingService;
import com.project.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final UserService userService;

    @DeleteMapping("/{bookingId}")
    public ApiResponse<Void> cancelBooking(
            @RequestHeader("Authorization") String token,
            @PathVariable String bookingId
    ) {
        String userId = userService.getUserIdFromToken(token.substring("Bearer ".length()));
        bookingService.cancelBooking(userId, bookingId);
        return ApiResponse.success();
    }
}
