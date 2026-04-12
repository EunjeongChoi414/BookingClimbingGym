package com.project.gym.repository;

import com.project.common.exception.DomainException;
import com.project.common.exception.ErrorCode;
import com.project.gym.entity.Order;
import com.project.gym.entity.OrderStatus;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository jpaRepository;

    public OrderRepositoryImpl(OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Order order) {
        jpaRepository.save(order);
    }

    @Override
    public Order getById(String orderId) {
        return jpaRepository.findById(orderId)
                .orElseThrow(() -> new DomainException(ErrorCode.ORDER_NOT_FOUND));
    }

    @Override
    public boolean existsPending(String userId, String passId) {
        return jpaRepository.existsByUserIdAndPassIdAndStatus(userId, passId, OrderStatus.PENDING);
    }
}
