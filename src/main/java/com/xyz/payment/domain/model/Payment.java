package com.xyz.payment.domain.model;

import com.xyz.payment.domain.exception.InvalidPaymentStateException;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Payment {
  String id;

  PaymentStatus status;

  List<PaymentItem> items;

  LocalDateTime updatedDateTime;

  public static Payment create(PaymentItem paymentItem) {
    var payment = new Payment();
    payment.setStatus(PaymentStatus.CREATED);
    payment.setItems(List.of(paymentItem));
    payment.setUpdatedDateTime(LocalDateTime.now());
    return payment;
  }

  public Payment complete() {
    validateStatus();
    setStatus(PaymentStatus.COMPLETED);
    setUpdatedDateTime(LocalDateTime.now());
    return this;
  }

  public Payment addItem(PaymentItem orderItem) {
    validateStatus();
    items.add(orderItem);
    setUpdatedDateTime(LocalDateTime.now());
    return this;
  }

  public BigDecimal getTotalAmount() {
    return items.stream().map(PaymentItem::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private void validateStatus() {
    if (status != PaymentStatus.CREATED) {
      throw new InvalidPaymentStateException("Order has invalid status:" + status);
    }
  }
}
