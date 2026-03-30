package com.project.infra.gym;

import com.project.domain.gym.Order;
import com.project.domain.gym.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, String> {
    boolean existsByUserIdAndPassIdAndStatus(String userId, String passId, OrderStatus status);
}
