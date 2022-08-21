package com.xyz.order.application;

import com.xyz.order.domain.command.AddOrderItem;
import com.xyz.order.domain.command.CompleteOrder;
import com.xyz.order.domain.command.CreateOrder;
import com.xyz.order.domain.exception.OrderNotFoundException;
import com.xyz.order.domain.model.Order;
import com.xyz.order.domain.model.OrderItem;
import com.xyz.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Mono<Order> createOrder(CreateOrder command) {
        final var order = Order.create(OrderItem.of(command.getAmount()));
        return orderRepository.save(order);
    }

    public Mono<Order> addOrderItem(AddOrderItem command) {
        return findOrder(command.getOrderId())
                .map(order -> order.addItem(OrderItem.of(command.getAmount())))
                .flatMap(orderRepository::save);
    }

    public Mono<Order> completeOrder(CompleteOrder command) {
        return findOrder(command.getOrderId())
                .map(Order::complete)
                .flatMap(orderRepository::save);
    }

    public Mono<Order> getOrder(String orderId) {
        return findOrder(orderId);
    }

    private Mono<Order> findOrder(String orderId) {
        return orderRepository
                .findById(orderId)
                .switchIfEmpty(
                        Mono.error(new OrderNotFoundException("Order ID Not found :" + orderId)));
    }
}
