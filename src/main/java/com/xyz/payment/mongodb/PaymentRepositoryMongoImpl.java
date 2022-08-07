package com.xyz.payment.mongodb;

import com.xyz.payment.domain.model.Payment;
import com.xyz.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryMongoImpl implements PaymentRepository {

  private final PaymentMongoRepository paymentMongoRepository;

  @Override
  public Mono<Payment> findById(String paymentId) {
    return paymentMongoRepository.findById(paymentId);
  }

  @Override
  public Mono<Payment> savePayment(Payment payment) {
    return paymentMongoRepository.save(payment);
  }
}
