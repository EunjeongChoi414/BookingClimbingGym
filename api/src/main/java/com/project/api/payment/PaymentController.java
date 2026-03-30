package com.project.api.payment;

import com.project.services.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 토스페이먼츠 sdk 에 결제 요청 이후 클라이언트에 반환되는 콜백
 *
 */
@RestController
@RequestMapping("/app/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


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
}
