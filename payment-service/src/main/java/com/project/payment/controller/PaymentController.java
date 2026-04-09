package com.project.payment.controller;

import com.project.common.jwt.JwtRequired;
import com.project.common.response.BaseResponse;
import com.project.common.response.ResponseService;
import com.project.payment.dto.*;
import com.project.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 결제 관련 컨트롤러
 * - 토스페이먼츠 SDK 결제 요청 이후 콜백 처리 (/app/payment)
 * - Pass 카드 구매 결제 준비/확인 (/app/gyms/{gymId}/passes/{passId}/payment)
 */
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final ResponseService responseService = new ResponseService();
    private final PaymentService paymentService;

    // 토스페이먼츠 결제 성공 콜백
    @GetMapping("/app/payment/success")
    public String paymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount
    ) {
        System.out.println("paymentKey = " + paymentKey);
        System.out.println("orderId = " + orderId);
        System.out.println("amount = " + amount);
        // 클라이언트가 이후에 "결제 Confirm API" 호출한다.
        return "success";
    }

    // 토스페이먼츠 결제 실패 콜백
    @GetMapping("/app/payment/fail")
    public ResponseEntity<?> paymentFail(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String orderId
    ) {
        System.out.println("결제 실패");
        System.out.println("code = " + code);
        System.out.println("message = " + message);
        System.out.println("orderId = " + orderId);

        paymentService.confirmFailOrder(orderId);

        return ResponseEntity.ok(Map.of(
                "status", "FAIL",
                "code", code,
                "message", message
        ));
    }

    // Pass 카드 구매 — 1단계: 결제 준비 (orderId 발급)
    @JwtRequired
    @PostMapping("/app/gyms/{gymId}/passes/{passId}/payment/prepare")
    public BaseResponse<PreparePaymentRes> preparePayment(
            @RequestAttribute(name = "userId") String userId,
            @PathVariable String gymId,
            @PathVariable String passId) {
        PrepareOrderModel model = paymentService.prepareOrder(userId, gymId, passId);

        return responseService.getSuccessResponse(
                new PreparePaymentRes(model.orderId(), model.amount(), model.passName()));
    }

    // Pass 카드 구매 — 2단계: 결제 확인 & UserPass 발급
    @JwtRequired
    @PostMapping("/app/gyms/{gymId}/passes/{passId}/payment/confirm")
    public BaseResponse<ConfirmPaymentRes> confirmPayment(
            @RequestAttribute(name = "userId") String userId,
            @PathVariable String gymId,
            @PathVariable String passId,
            @RequestBody @Valid ConfirmPaymentReq req) {

        ConfirmedPassModel model = paymentService.confirmOrder(
                userId, gymId, passId, req.paymentKey(), req.orderId(), req.amount());

        return responseService.getSuccessResponse(
                new ConfirmPaymentRes(model.userPassId(), model.passName(), model.validUntil(), model.remainingUses()));
    }
}