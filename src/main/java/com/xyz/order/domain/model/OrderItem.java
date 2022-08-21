package com.xyz.order.domain.model;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@ToString
public class OrderItem {
  String id;

  BigDecimal amount;

  public static OrderItem of(BigDecimal amount) {
    final var orderItem = new OrderItem();
    orderItem.setId(UUID.randomUUID().toString());
    orderItem.setAmount(amount);
    return orderItem;
  }
}
