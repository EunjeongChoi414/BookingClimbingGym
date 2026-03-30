package com.project.infra.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.domain.gym.PaymentClient;
import com.project.domain.gym.PaymentConfirmResult;
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
    private final ObjectMapper objectMapper;

    public TossPaymentClientImpl(
            @Value("${toss.secret-key}") String secretKey,
            RestTemplate restTemplate,
            ObjectMapper objectMapper) {
        this.secretKey = secretKey;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
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
            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    CONFIRM_URL, HttpMethod.POST, request, JsonNode.class);

            String receiptUrl = response.getBody()
                    .path("receipt")
                    .path("url")
                    .asText(null);

            return PaymentConfirmResult.success(receiptUrl);

        } catch (HttpClientErrorException e) {
            String errorMessage = extractErrorMessage(e.getResponseBodyAsString());
            return PaymentConfirmResult.failure(errorMessage);
        }
    }

    private HttpHeaders buildHeaders() {
        // 토스 인증: Base64("secretKey:")
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
