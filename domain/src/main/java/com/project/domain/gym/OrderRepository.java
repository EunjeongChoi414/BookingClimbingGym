package com.project.domain.gym;

public interface OrderRepository {
    void save(Order order);
    Order getById(String orderId);
    boolean existsPending(String userId, String passId);
}
