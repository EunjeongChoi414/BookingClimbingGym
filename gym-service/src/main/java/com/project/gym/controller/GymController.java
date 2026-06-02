package com.project.gym.controller;

import com.project.common.jwt.JwtRequired;
import com.project.common.response.ApiResponse;
import com.project.gym.dto.*;
import com.project.gym.service.GymService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/app/gyms")
@RequiredArgsConstructor
public class GymController {

    private final GymService gymService;

    //관리자 api: 사업자 정보 인증하기
    @PostMapping("/business-verification")
    public CompletableFuture<ApiResponse<String>> verifyBusiness(
            @RequestBody @Valid VerifyBusinessReq req) {

        return gymService.verifyBusiness(
                        req.getBusinessRegistrationNumber(),
                        req.getLegalRepresentativeName(),
                        req.getBusinessStartDate())
                .thenApply(ApiResponse::success);
    }

    //관리자 api: 정산 계좌 인증하기
    @PostMapping("/payment-account-verification")
    public ApiResponse<Void> paymentAccountVerification(
            @RequestBody @Valid PaymentAccountVerificationReq req
    ) {
        gymService.verifyBusinessRepresentative(
                req.getTicket(), req.getAccountHolderName(), req.getBankName(), req.getAccountNumber());

        return ApiResponse.success();
    }

    //관리자 api: 암장 등록하기
    @JwtRequired
    @PostMapping("")
    public ApiResponse<String> registerGym(
            @RequestAttribute(name = "userId") String userId,
            @RequestBody @Valid RegisterGymReq req) {
        List<BusinessHoursModel> businessHoursModels = GymDtoMapper.toBusinessHoursModels(req.getBusinessHours());
        List<PassModel> passInputs = GymDtoMapper.toPassModels(req.getPasses());

        String gymId = gymService.registerGym(req.getGymName(), req.getGymAddress(), req.getContact(),
                businessHoursModels, passInputs, userId, req.getMaxCapacity(), req.getCancellationNoticeDays());

        return ApiResponse.success(gymId);
    }

    //암장 필터로 조회하기
    @GetMapping("")
    public ApiResponse<GetGymsRes> getGyms(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(30) int size,
            @RequestParam(required = false) String keyword) {
        List<GymPreviewModel> gymModels = gymService.getGyms(page, size, keyword);

        List<Gym> gyms = new ArrayList<>();
        gymModels.forEach(gym -> {
            List<BusinessHours> businessHours = GymDtoMapper.toBusinessHoursDtos(gym.businessHours());
            gyms.add(new Gym(gym.name(), businessHours, gym.currentCrowdLevel(), gym.address()));
        });

        return ApiResponse.success(new GetGymsRes(gyms));
    }

    //특정 암장 상세 정보
    @GetMapping("/{gymId}")
    public ApiResponse<GetGymDetailRes> getGymDetail(
            @PathVariable String gymId) {

        GymDetailModel gymDetail = gymService.getGymDetail(gymId);
        List<BusinessHours> businessHours = GymDtoMapper.toBusinessHoursDtos(gymDetail.businessHours());
        List<Pass> passes = GymDtoMapper.toPassDtos(gymDetail.passes());

        var res = new GetGymDetailRes(
                gymDetail.name(),
                businessHours,
                gymDetail.isBusy(),
                gymDetail.address(),
                passes,
                gymDetail.contact());

        return ApiResponse.success(res);
    }

    //특정 암장에 대한 유저 정보
    @JwtRequired
    @GetMapping("/{gymId}/me")
    public ApiResponse<GetGymMyDetailRes> getGymMyDetail(
            @RequestAttribute(name = "userId") String userId,
            @PathVariable String gymId
    ) {
        UserGymModel userGymModel = gymService.getUserInfoFromGym(gymId, userId);

        return ApiResponse.success(GetGymMyDetailRes.from(userGymModel));
    }

    //특정 날짜 & 시간대의 바쁜 정도 (예약 정도) 조회
    @GetMapping("/{gymId}/crowdedness")
    public ApiResponse<String> getGymCrowdedness(
            @PathVariable String gymId,
            @RequestBody @Valid GetGymCrowdednessReq req) {

        String crowdedness = gymService.getCrowdedness(gymId, req.getDateTime());

        return ApiResponse.success(crowdedness);
    }

    //관리자 api: 암장 결제 건들 보여주기
    @JwtRequired
    @GetMapping("/{gymId}/bookings")
    public ApiResponse<GetGymBookingsRes> getGymBookings(
            @RequestAttribute(name = "userId") String userId,
            @PathVariable String gymId) {
        List<BookingModel> bookingModels = gymService.getGymBookings(userId, gymId);
        List<Booking> bookings = GymDtoMapper.toBookingDtos(bookingModels);

        return ApiResponse.success(new GetGymBookingsRes(bookings));
    }

    //패스로 결제하기
    @JwtRequired
    @PostMapping("/{gymId}/pass-redemption")
    public ApiResponse<PayWithPassRes> payWithPass(
            @RequestAttribute(name = "userId") String userId,
            @PathVariable String gymId,
            @RequestBody @Valid PayWithPassReq req) {
        BookedWithPassModel bookedWithPassModel = gymService.bookGymWithPass(userId, gymId, req.getPassId(), req.getStartDateTime());

        var res = new PayWithPassRes(
                bookedWithPassModel.bookingId(),
                bookedWithPassModel.remainingUses(),
                bookedWithPassModel.qrToken());

        return ApiResponse.success(res);
    }
}
