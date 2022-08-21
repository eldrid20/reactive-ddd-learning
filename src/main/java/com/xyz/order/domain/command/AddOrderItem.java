package com.xyz.order.domain.command;

import lombok.Value;

import java.math.BigDecimal;

@Value(staticConstructor = "of")
public class AddOrderItem {
    String orderId;
    BigDecimal amount;
}
