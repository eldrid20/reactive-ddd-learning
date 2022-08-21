package com.xyz.order.domain.model;

import com.xyz.order.domain.exception.InvalidOrderStateException;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Order {
    String id;

    OrderStatus status;

    List<OrderItem> items;

    LocalDateTime updatedDateTime;

    public static Order create(OrderItem orderItem) {
        var order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setItems(List.of(orderItem));
        order.setUpdatedDateTime(LocalDateTime.now());
        return order;
    }

    public Order complete() {
        validateStatus();
        setStatus(OrderStatus.COMPLETED);
        setUpdatedDateTime(LocalDateTime.now());
        return this;
    }

    public Order addItem(OrderItem orderItem) {
        validateStatus();
        items.add(orderItem);
        setUpdatedDateTime(LocalDateTime.now());
        return this;
    }

    public BigDecimal getTotalAmount() {
        return items.stream().map(OrderItem::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateStatus() {
        if (status != OrderStatus.CREATED) {
            throw new InvalidOrderStateException("Order has invalid status:" + status);
        }
    }
}
