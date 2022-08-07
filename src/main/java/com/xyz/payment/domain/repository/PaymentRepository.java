package com.xyz.payment.domain.repository;

import com.xyz.payment.domain.model.Payment;
import reactor.core.publisher.Mono;

public interface PaymentRepository {
  Mono<Payment> findById(String paymentId);

  Mono<Payment> savePayment(Payment payment);
}
