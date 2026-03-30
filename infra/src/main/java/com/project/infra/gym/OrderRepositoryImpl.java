package com.project.infra.gym;

import com.project.domain.exception.OrderNotFoundException;
import com.project.domain.gym.Order;
import com.project.domain.gym.OrderRepository;
import com.project.domain.gym.OrderStatus;
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
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public boolean existsPending(String userId, String passId) {
        return jpaRepository.existsByUserIdAndPassIdAndStatus(userId, passId, OrderStatus.PENDING);
    }
}
