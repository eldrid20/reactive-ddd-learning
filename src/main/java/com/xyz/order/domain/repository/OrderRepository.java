package com.xyz.order.domain.repository;

import com.xyz.order.domain.model.Order;
import reactor.core.publisher.Mono;

public interface OrderRepository {
    Mono<Order> findById(String orderId);

  Mono<Order> save(Order order);
}
