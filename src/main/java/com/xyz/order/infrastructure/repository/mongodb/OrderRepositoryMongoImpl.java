package com.xyz.order.infrastructure.repository.mongodb;

import com.xyz.order.domain.model.Order;
import com.xyz.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryMongoImpl implements OrderRepository {

    private final OrderMongoRepository orderMongoRepository;

    @Override
    public Mono<Order> findById(String orderId) {
        return orderMongoRepository.findById(orderId);
    }

    @Override
    public Mono<Order> save(Order order) {
        return orderMongoRepository.save(order);
    }
}
