package com.project.api.booking;

import com.project.api.response.BaseResponse;
import com.project.api.response.ResponseService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/bookings")
public class BookingController {
    private final ResponseService responseService = new ResponseService();

    @DeleteMapping("/{bookingId}")
    public BaseResponse<String> cancelBooking(
            @RequestHeader("Authorization") String token,
            @PathVariable String bookingId
    ){
        //bookingId 로 존재하는 예약건인지 & 요청한 사람이 예약한 사람인지 등 확인.

        return responseService.getSuccessResponse();
    }
}
