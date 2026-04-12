package com.project.payment.repository;

import com.project.common.exception.DomainException;
import com.project.common.exception.ErrorCode;
import com.project.gym.entity.Order;
import com.project.gym.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {

    boolean existsByUserIdAndPassIdAndStatus(String userId, String passId, OrderStatus status);

    default Order getById(String orderId) {
        return findById(orderId)
                .orElseThrow(() -> new DomainException(ErrorCode.ORDER_NOT_FOUND));
    }

    default boolean existsPending(String userId, String passId) {
        return existsByUserIdAndPassIdAndStatus(userId, passId, OrderStatus.PENDING);
    }
}
