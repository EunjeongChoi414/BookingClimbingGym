package com.project.api.gym;

import com.project.api.gym.dto.PayWithPassReq;
import com.project.api.gym.dto.PayWithPassRes;
import com.project.api.gym.dto.*;
import com.project.api.response.BaseResponse;
import com.project.api.response.ResponseService;
import com.project.api.response.exception.ExceptionStatus;
import com.project.services.booking.model.BookingDetailModel;
import com.project.services.gym.GymService;
import com.project.services.gym.model.*;
import com.project.services.user.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/app/gyms")
@RequiredArgsConstructor
public class GymController {

    private final ResponseService responseService = new ResponseService();
    private final GymService gymService;
    private final UserService userService;

    //관리자 api: 사업자 정보 인증하기
    @PostMapping("/business-verification")
    public BaseResponse<String> verifyBusiness(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid VerifyBusinessReq req) {

        String ticket = gymService.verifyBusiness(req.getBusinessRegistrationNumber(), req.getLegalRepresentativeName(), req.getBusinessStartDate());

        return responseService.getSuccessResponse(ticket);
    }

    //관리자 api: 정산 계좌 인증하기
    @PostMapping("/payment-account-verification")
    public BaseResponse<String> paymentAccountVerification(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid PaymentAccountVerificationReq req
    ){
        gymService.verifyBusinessRepresentative(
                req.getTicket(), req.getAccountHolderName(), req.getBankName(), req.getAccountNumber());

        return responseService.getSuccessResponse();
    }

    //관리자 api: 암장 등록하기
    @PostMapping("")
    public BaseResponse<String> registerGym(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid RegisterGymReq req){

        // 영업시간 검증
        for (BusinessHours bh : req.getBusinessHours()) {
            var start = bh.getStartTime();
            var end = bh.getEndTime();
            if (start == null && end == null) continue;
            if (start == null || end == null || end.isBefore(start)) {
                return responseService.getFailureResponse(ExceptionStatus.INVALID_BUSINESS_HOURS);
            }
        }

        List<BusinessHoursModel> businessHoursInputs = new ArrayList<>();
        for (BusinessHours bh : req.getBusinessHours()) {
            businessHoursInputs.add(new BusinessHoursModel(bh.getDay(), bh.getStartTime(), bh.getEndTime()));
        }

        List<PassModel> passInputs = new ArrayList<>();
        for (Pass p : req.getPasses()) {
            passInputs.add(new PassModel(p.getName(), p.getPrice(), p.getMaxUses(), p.getValidDays()));
        }

        String userId = userService.getUserIdFromToken(token.substring("Bearer ".length()));
        String gymId = gymService.registerGym(req.getGymName(), req.getGymAddress(), req.getContact(),
                businessHoursInputs, passInputs, userId, req.getMaxCapacity());

        return responseService.getSuccessResponse(gymId);
    }

    //암장 필터로 조회하기
    @GetMapping("")
    public BaseResponse<GetGymsRes> getGyms(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(30) int size,
            @RequestParam(required = false) String keyword) {
        List<GymPreviewModel> gymModels = gymService.getGyms(page, size, keyword);

        List<Gym> gyms = new ArrayList<>();
        gymModels.forEach(gym -> {
            List<BusinessHours> businessHours = new ArrayList<>();
            for (BusinessHoursModel bhModel : gym.businessHours()) {
                businessHours.add(new  BusinessHours(bhModel.getDay(), bhModel.getStartTime(), bhModel.getEndTime()));
            }
            gyms.add(new Gym(gym.name(), businessHours, gym.currentCrowdLevel(), gym.address()));
        });

        return responseService.getSuccessResponse(new GetGymsRes(gyms));
    }

    //특정 암장 상세 정보
    @GetMapping("/{gymId}")
    public BaseResponse<GetGymDetailRes> getGymDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable String gymId){

        GymDetailModel gymDetail = gymService.getGymDetail(gymId);
        List<BusinessHours> businessHours = new ArrayList<>();
        for (BusinessHoursModel hours : gymDetail.businessHours()) {
            businessHours.add(new BusinessHours(hours.getDay(), hours.getStartTime(), hours.getEndTime()));
        }
        List<Pass> passes = new ArrayList<>();
        for (PassModel p : gymDetail.passes()) {
            passes.add(new Pass(p.getName(), p.getPrice(), p.getMaxUses(), p.getValidDays()));
        }

        var res = new GetGymDetailRes(
                gymDetail.name(),
                businessHours,
                gymDetail.isBusy(),
                gymDetail.address(),
                passes,
                gymDetail.contact());

        return responseService.getSuccessResponse(res);
    }

    //특정 암장에 대한 유저 정보
    @GetMapping("/{gymId}/me")
    public BaseResponse<GetGymMyDetailRes> getGymMyDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable String gymId
    ){
        String userId = userService.getUserIdFromToken(token.substring("Bearer ".length()));
        UserGymModel userGymModel = gymService.getUserInfoFromGym(gymId, userId);

        return responseService.getSuccessResponse(GetGymMyDetailRes.from(userGymModel));
    }

    //특정 날짜 & 시간대의 바쁜 정도 (예약 정도) 조회
    @GetMapping("/{gymId}/crowdedness")
    public BaseResponse<String> getGymCrowdedness(
            @RequestHeader("Authorization") String token,
            @PathVariable String gymId,
            @RequestBody @Valid GetGymCrowdednessReq req){

        String crowdedness = gymService.getCrowdedness(gymId, req.getDateTime());

        return responseService.getSuccessResponse(crowdedness);
    }

    //관리자 api: 암장 결제 건들 보여주기
    @GetMapping("/{gymId}/bookings")
    public BaseResponse<GetGymBookingsRes> getGymBookings(
            @RequestHeader("Authorization") String token,
            @PathVariable String gymId){
        String userId = userService.getUserIdFromToken(token.substring("Bearer ".length()));
        List<BookingDetailModel> bookingModels = gymService.getGymBookings(userId, gymId);

        List<BookingDetail> bookings = new ArrayList<>();
        bookingModels.forEach(b -> {
            bookings.add(new BookingDetail(b.id(), b.userId(), b.dateTime(), b.passId()));
        });

        return responseService.getSuccessResponse(new GetGymBookingsRes(bookings));
    }

    //패스로 결제하기
    @PostMapping("/{gymId}/pass-redemption")
    public BaseResponse<PayWithPassRes> payWithPass(
            @RequestHeader("Authorization") String token,
            @PathVariable String gymId,
            @RequestBody @Valid PayWithPassReq req){

        String userId = userService.getUserIdFromToken(token.substring("Bearer ".length()));

        BookedWithPassModel bookedWithPassModel = gymService.bookGymWithPass(userId, gymId, req.getPassId(), req.getStartDateTime());

        var res = new PayWithPassRes(
                bookedWithPassModel.bookingId(),
                bookedWithPassModel.remainingUses(),
                bookedWithPassModel.qrToken());

        return responseService.getSuccessResponse(res);
    }
}
