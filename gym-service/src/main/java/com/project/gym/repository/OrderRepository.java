package com.project.gym.repository;

import com.project.gym.entity.Order;

public interface OrderRepository {
    void save(Order order);
    Order getById(String orderId);
    boolean existsPending(String userId, String passId);
}
