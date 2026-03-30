package com.project.domain.gym;

import com.project.domain.exception.AlreadyProcessedException;
import com.project.domain.exception.AmountMismatchException;
import com.project.domain.exception.OrderExpiredException;
import com.project.domain.exception.UnauthorizedException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String id; // orderId — 토스 orderId 와 1:1 매핑

    private String userId;
    private String passId;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String paymentKey;
    private String receiptUrl;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime completedAt;

    protected Order() {
    }

    public Order(String userId, String passId, BigDecimal amount, LocalDateTime now) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.passId = passId;
        this.amount = amount;
        this.status = OrderStatus.PENDING;
        this.createdAt = now;
        this.expiresAt = now.plusMinutes(10);
    }

    /**
     * confirm 단계 진입 전 검증.
     * 위변조 방지: userId, amount 를 서버 저장값과 비교
     */
    public void validateForConfirm(
            String userId, BigDecimal clientAmount, LocalDateTime now) {
        if (!this.userId.equals(userId)) {
            throw new UnauthorizedException();
        }
        if (this.amount.compareTo(clientAmount) != 0) {
            throw new AmountMismatchException();
        }
        if (this.status != OrderStatus.PENDING) {
            throw new AlreadyProcessedException();
        }
        if (now.isAfter(this.expiresAt)) {
            throw new OrderExpiredException();
        }
    }

    public void complete(String paymentKey, String receiptUrl) {
        this.status = OrderStatus.COMPLETED;
        this.paymentKey = paymentKey;
        this.receiptUrl = receiptUrl;
        this.completedAt = LocalDateTime.now();
    }

    public void fail() {
        this.status = OrderStatus.FAILED;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassId() {
        return passId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getPaymentKey() {
        return paymentKey;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
}
