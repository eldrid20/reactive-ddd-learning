package com.xyz.payment.domain.model;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@ToString
public class PaymentItem {
  String id;

  BigDecimal amount;

  public static PaymentItem of(BigDecimal amount) {
    final var paymentItem = new PaymentItem();
    paymentItem.setId(UUID.randomUUID().toString());
    paymentItem.setAmount(amount);
    return paymentItem;
  }
}
