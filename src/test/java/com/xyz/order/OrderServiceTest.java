package com.xyz.order;

import com.xyz.order.application.OrderService;
import com.xyz.order.domain.command.AddOrderItem;
import com.xyz.order.domain.command.CompleteOrder;
import com.xyz.order.domain.command.CreateOrder;
import com.xyz.order.domain.exception.OrderNotFoundException;
import com.xyz.order.domain.model.Order;
import com.xyz.order.domain.model.OrderStatus;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void createOrderByCommand_ShouldCreatedSuccessfully() {
        // given
        final var command = CreateOrder.of(BigDecimal.valueOf(10.00));

        // when
        final var order = orderService.createOrder(command).block();

        // then
        assert order != null;
        assertThat(order.getTotalAmount().compareTo(BigDecimal.valueOf(10.00))).isZero();
    }

    @Test
    void addOrderItemByCommand_ShouldBeAddedSuccessfully() {
        // given
        final var createOrderCommand = CreateOrder.of(BigDecimal.valueOf(10.00));
        orderService
                .createOrder(createOrderCommand)
                // when
                .flatMap(
                        order -> {
                            final var addItemCommand =
                                    AddOrderItem.of(order.getId(), BigDecimal.valueOf(35.00));
                            return orderService.addOrderItem(addItemCommand);
                        })
                // then
                .doOnSuccess(order -> verifyTotalOrderItem(order, BigDecimal.valueOf(45.00)))
                .subscribe();
    }

    @Test
    void addOrderItemByCommand_WhenInvalidOrderIdSupplied_ShouldThrowOrderNotFoundException() {
        // given
        final var addOrderItemCommand = AddOrderItem.of("-1", BigDecimal.valueOf(12));
        // when & then
        StepVerifier.create(orderService.addOrderItem(addOrderItemCommand))
                .expectError(OrderNotFoundException.class)
                .verify();
    }

    @Test
    void completeOrderByCommand_ShouldBeProceedSuccessfully() {
        // given
        final var createOrderCommand = CreateOrder.of(BigDecimal.valueOf(10.00));
        orderService
                .createOrder(createOrderCommand)
                // when
                .flatMap(
                        updatedPayment -> {
                            final var completeCommand = CompleteOrder.of(updatedPayment.getId());
                            return orderService.completeOrder(completeCommand);
                        })
                // then
                .doOnSuccess(this::verifyOrderCompleted)
                .subscribe();
    }

    @NotNull
    private Mono<Order> verifyOrderCompleted(Order order) {
        return orderService
                .getOrder(order.getId())
                .doOnSuccess(
                        order1 -> assertThat(order1.getStatus()).isEqualTo(OrderStatus.COMPLETED));
    }

    @NotNull
    private Mono<Order> verifyTotalOrderItem(Order order, BigDecimal totalAmount) {
        return orderService
                .getOrder(order.getId())
                .doOnSuccess(
                        updatedOrder ->
                                assertThat(updatedOrder.getTotalAmount().compareTo(totalAmount)).isZero());
    }

    @Test
    void completeOrderByCommand_WhenInvalidOrderIdSupplied_ShouldThrowOrderNotFoundException() {
        // given
        final var completeCommand = CompleteOrder.of("-1");
        // when & then
        StepVerifier.create(orderService.completeOrder(completeCommand))
                .expectError(OrderNotFoundException.class)
                .verify();
    }
}
