package com.project.gym.controller;

import com.project.common.response.BaseResponse;
import com.project.common.response.ResponseService;
import com.project.gym.service.BookingService;
import com.project.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final ResponseService responseService = new ResponseService();
    private final BookingService bookingService;
    private final UserService userService;

    @DeleteMapping("/{bookingId}")
    public BaseResponse<String> cancelBooking(
            @RequestHeader("Authorization") String token,
            @PathVariable String bookingId
    ){
        String userId = userService.getUserIdFromToken(token.substring("Bearer ".length()));
        bookingService.cancelBooking(userId, bookingId);
        return responseService.getSuccessResponse();
    }
}
