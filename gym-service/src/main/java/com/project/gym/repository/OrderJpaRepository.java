package com.project.gym.repository;

import com.project.gym.entity.Order;
import com.project.gym.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, String> {
    boolean existsByUserIdAndPassIdAndStatus(String userId, String passId, OrderStatus status);
}
