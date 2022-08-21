package com.xyz.order.adapter.incoming.rest;

import com.xyz.order.adapter.incoming.rest.dto.OrderDto;
import com.xyz.order.application.OrderService;
import com.xyz.order.domain.command.AddOrderItem;
import com.xyz.order.domain.command.CompleteOrder;
import com.xyz.order.domain.command.CreateOrder;
import com.xyz.order.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping(
      value = "/v1/orders",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Order> createOrder(@RequestBody @Valid OrderDto orderDto) {
    return orderService.createOrder(CreateOrder.of(orderDto.getAmount()));
  }

  @PutMapping(
      value = "/v1/orders/{orderId}/items",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Order> addOrderItem(
          @PathVariable String orderId, @RequestBody @Valid OrderDto orderDto) {
    return orderService.addOrderItem(
        AddOrderItem.of(orderId, orderDto.getAmount()));
  }

  @PutMapping(
      value = "/v1/orders/{orderId}/complete",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Order> completeOrder(@PathVariable String orderId) {
    return orderService.completeOrder(CompleteOrder.of(orderId));
  }
}
