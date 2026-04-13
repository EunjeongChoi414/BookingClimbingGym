package com.project.gym.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.gym.entity.PaymentConfirmResult;
import com.project.gym.port.PaymentClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class TossPaymentClientImpl implements PaymentClient {

    private static final String CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";

    private final String secretKey;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TossPaymentClientImpl(
            @Value("${toss.secret-key}") String secretKey,
            RestTemplate restTemplate) {
        this.secretKey = secretKey;
        this.restTemplate = restTemplate;
    }

    @Override
    public PaymentConfirmResult confirm(String paymentKey, String orderId, BigDecimal amount) {
        HttpHeaders headers = buildHeaders();

        Map<String, Object> body = new HashMap<>();
        body.put("paymentKey", paymentKey);
        body.put("orderId", orderId);
        body.put("amount", amount);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    CONFIRM_URL, HttpMethod.POST, request, String.class);

            JsonNode node = objectMapper.readTree(response.getBody());

            String receiptUrl = node.path("receipt")
                    .path("url")
                    .asText(null);

            return PaymentConfirmResult.success(receiptUrl);

        } catch (HttpClientErrorException e) {
            String errorMessage = extractErrorMessage(e.getResponseBodyAsString());
            return PaymentConfirmResult.failure(errorMessage);
        } catch (Exception e) {
            return PaymentConfirmResult.failure("결제 승인 응답 처리에 실패했습니다.");
        }
    }

    private HttpHeaders buildHeaders() {
        String encoded = Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + encoded);
        return headers;
    }

    private String extractErrorMessage(String responseBody) {
        try {
            JsonNode node = objectMapper.readTree(responseBody);
            return node.path("message").asText("결제 승인에 실패했습니다.");
        } catch (Exception e) {
            return "결제 승인에 실패했습니다.";
        }
    }
}