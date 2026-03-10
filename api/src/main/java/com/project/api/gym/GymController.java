package com.project.api.gym;

import com.project.api.gym.dto.PayWithPassReq;
import com.project.api.gym.dto.PayWithPassRes;
import com.project.api.gym.dto.*;
import com.project.api.response.BaseResponse;
import com.project.api.response.ResponseService;
import com.project.api.response.exception.ExceptionStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/app/gyms")
public class GymController {

    private final ResponseService responseService = new ResponseService();

    //관리자 api: 사업자 정보 인증하기
    @PostMapping("/business-verification")
    public BaseResponse<String> verifyBusiness(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid VerifyBusinessReq req) {
        //유효한 사업자인지 req 로 확인한다 (ex. 홈택스 api)

        //유효하다면 ticket 에 사업주 이름을 넣어서 확인했음을 표시한다.
        String ticket = "validTicket";

        return responseService.getSuccessResponse(ticket);
    }

    //관리자 api: 정산 계좌 인증하기
    @PostMapping("/payment-account-verification")
    public BaseResponse<String> paymentAccountVerification(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid PaymentAccountVerificationReq req
    ){
        //1. ticket 으로 사업자 정보가 인증 되었는지 확인한다 -> 안 되었으면 그것먼저 해야함

        //2. accountHolder 이름이 ticket 에 있는 사업자 이름과 같은지 확인한다. -> 안 같으면 등록 불가.

        return responseService.getSuccessResponse();
    }

    //관리자 api: 암장 등록하기
    @PostMapping("")
    public BaseResponse<String> registerGym(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid RegisterGymReq req){

        // 영업시간 검증
        for (BusinessHours businessHours : req.getBusinessHours()){
            var start = businessHours.getStartTime();
            var end = businessHours.getEndTime();

            if (start == null && end == null) continue;

            if (start == null || end == null || end.isBefore(start)) {
                return responseService.getFailureResponse(ExceptionStatus.INVALID_BUSINESS_HOURS);
            }
        }

        // 등록하기

        return responseService.getSuccessResponse("gymId");
    }

    //암장 필터로 조회하기
    @GetMapping("")
    public BaseResponse<GetGymsRes> getGyms(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(30) int size,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String keyword) {
        //필터링 실시.

        //더미 데이터
        var gym = new Gym(
                "test gym",
                "https://photouri",
                true,
                "Moderate",
                "서울시 성동구 ...");
        var res = new GetGymsRes();
        res.getGyms().add(gym);

        return responseService.getSuccessResponse(res);
    }

    //특정 암장 상세 정보
    @GetMapping("/{gymId}")
    public BaseResponse<GetGymDetailRes> getGymDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable String gymId){
        //gymId 로 gym 조회

        //더미데이터
        List<BusinessHours> businessHours = new ArrayList<>();
        businessHours.add(new BusinessHours(DayOfWeek.MONDAY, null, null));
        businessHours.add(new BusinessHours(DayOfWeek.TUESDAY, LocalTime.parse("10:00"), LocalTime.parse("23:00")));
        businessHours.add(new BusinessHours(DayOfWeek.WEDNESDAY, LocalTime.parse("10:00"), LocalTime.parse("23:00")));
        businessHours.add(new BusinessHours(DayOfWeek.THURSDAY, LocalTime.parse("10:00"), LocalTime.parse("23:00")));
        businessHours.add(new BusinessHours(DayOfWeek.FRIDAY, LocalTime.parse("10:00"), LocalTime.parse("23:00")));
        businessHours.add(new BusinessHours(DayOfWeek.SATURDAY, LocalTime.parse("10:00"), LocalTime.parse("23:00")));
        businessHours.add(new BusinessHours(DayOfWeek.SUNDAY, LocalTime.parse("10:00"), LocalTime.parse("23:00")));

        List<Pass> passes = new ArrayList<>();
        passes.add(new Pass("일일회원권", new BigDecimal(20000), 1, 1));

        var res = new GetGymDetailRes(
                "test gym name",
                new ArrayList<>(List.of("https://photo1", "https://photo2")),
                false,
                businessHours,
                "Quiet",
                "서울시 성동구..",
                passes,
                "02-2222-2222");

        return responseService.getSuccessResponse(res);
    }

    //특정 암장에 대한 유저 정보
    @GetMapping("/{gymId}/me")
    public BaseResponse<GetGymMyDetailRes> getGymMyDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable String gymId
    ){
        //gymId 로 조회
        List<UserPass> myPasses = new ArrayList<>();
        myPasses.add(new UserPass(
                "passId",
                "5회 이용권",
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 6, 1), 4)
        );
        List<UserBooking> myBookings = new ArrayList<>();
        myBookings.add(new UserBooking(
                "bookingId",
                LocalDate.of(2026, 3, 5),
                LocalTime.parse("10:00")
        ));

        var res = new GetGymMyDetailRes(myPasses, myBookings);
        return responseService.getSuccessResponse(res);
    }

    //특정 날짜 & 시간대의 바쁜 정도 (예약 정도) 조회
    @GetMapping("/{gymId}/crowdedness")
    public BaseResponse<String> getGymCrowdedness(
            @RequestHeader("Authorization") String token,
            @PathVariable String gymId,
            @RequestBody @Valid GetGymCrowdednessReq req){

        if(req.getStartTime().isAfter(req.getEndTime())){
            return responseService.getFailureResponse(ExceptionStatus.INVALID_HOURS);
        }

        return responseService.getSuccessResponse("Moderate");
    }

    //관리자 api: 암장 결제 건들 보여주기
    @GetMapping("/{gymId}/bookings")
    public BaseResponse<GetGymBookingsRes> getGymBookings(
            @RequestHeader("Authorization") String token,
            @PathVariable String gymId){
        //토큰으로 관리자인지 확인

        //예약건들을 조회하여 반환
        List<BookingDetail> bookings = new ArrayList<>();
        bookings.add(new BookingDetail("bookingId", "userId",
                LocalDateTime.of(2026, 3, 5, 10, 0),
                "passId"));

        var res = new GetGymBookingsRes(bookings);
        return responseService.getSuccessResponse(res);
    }

    @PostMapping("/{gymId}/pass-redemption")
    public BaseResponse<PayWithPassRes> payWithPass(
            @RequestHeader("Authorization") String token,
            @PathVariable String gymId,
            @RequestBody @Valid PayWithPassReq req){

        //유저가 가지고 있는 passId 가 맞는지 확인

        var res = new PayWithPassRes("bookingId", 2, "qrtoken");
        return responseService.getSuccessResponse(res);
    }
}
