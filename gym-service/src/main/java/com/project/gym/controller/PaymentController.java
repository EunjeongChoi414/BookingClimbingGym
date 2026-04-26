package com.project.gym.controller;

import com.project.common.jwt.JwtRequired;
import com.project.common.response.ApiResponse;
import com.project.gym.dto.ConfirmPaymentReq;
import com.project.gym.dto.ConfirmPaymentRes;
import com.project.gym.dto.PrepareOrderModel;
import com.project.gym.dto.PreparePaymentRes;
import com.project.gym.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 결제 관련 컨트롤러
 * - 토스페이먼츠 SDK 결제 요청 이후 콜백 처리 (/app/payment)
 * - Pass 카드 구매 결제 준비/확인 (/app/gyms/{gymId}/passes/{passId}/payment)
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/app/payment")
public class PaymentController {

    private final PaymentService paymentService;

    // 토스페이먼츠 결제 성공 콜백
    @GetMapping("/success")
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
    @GetMapping("/fail")
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
    @PostMapping("prepare/gyms/{gymId}/passes/{passId}")
    public ApiResponse<PreparePaymentRes> preparePayment(
            @RequestAttribute(name = "userId") String userId,
            @PathVariable String gymId,
            @PathVariable String passId) {
        PrepareOrderModel model = paymentService.prepareOrder(userId, gymId, passId);

        return ApiResponse.success(new PreparePaymentRes(model.orderId(), model.amount(), model.passName()));
    }

    // Pass 카드 구매 — 2단계: 결제 확인 & UserPass 발급
    @JwtRequired
    @PostMapping("confirm/app/gyms/{gymId}/passes/{passId}")
    public CompletableFuture<ApiResponse<ConfirmPaymentRes>> confirmPayment(
            @RequestAttribute(name = "userId") String userId,
            @PathVariable String gymId,
            @PathVariable String passId,
            @RequestBody @Valid ConfirmPaymentReq req) {

        return paymentService.confirmOrder(
                        userId, gymId, passId, req.paymentKey(), req.orderId(), req.amount())
                .thenApply(model -> ApiResponse.success(
                        new ConfirmPaymentRes(model.userPassId(), model.passName(), model.validUntil(), model.remainingUses())));
    }
}
