package com.xyz.payment.mongodb;

import com.xyz.payment.domain.model.Payment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMongoRepository extends ReactiveMongoRepository<Payment, String> {}
