package com.project.gym.controller;

import com.project.common.jwt.JwtRequired;
import com.project.common.response.BaseResponse;
import com.project.common.response.ResponseService;
import com.project.gym.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final ResponseService responseService = new ResponseService();
    private final BookingService bookingService;

    @JwtRequired
    @DeleteMapping("/{bookingId}")
    public BaseResponse<String> cancelBooking(
            @RequestAttribute(name = "userId") String userId,
            @PathVariable String bookingId
    ) {
        bookingService.cancelBooking(userId, bookingId);
        return responseService.getSuccessResponse();
    }
}