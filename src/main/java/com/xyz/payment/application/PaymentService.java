package com.xyz.payment.application;

import com.xyz.payment.domain.command.AddPaymentItemCommand;
import com.xyz.payment.domain.command.CompletePaymentCommand;
import com.xyz.payment.domain.command.CreatePaymentCommand;
import com.xyz.payment.domain.exception.PaymentNotFoundException;
import com.xyz.payment.domain.model.Payment;
import com.xyz.payment.domain.model.PaymentItem;
import com.xyz.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public Mono<Payment> createPayment(CreatePaymentCommand command) {
        final var payment = Payment.create(PaymentItem.of(command.getAmount()));
        return paymentRepository.savePayment(payment);
    }

    public Mono<Payment> addPaymentItem(AddPaymentItemCommand command) {
        return findPayment(command.getPaymentId())
                .map(payment -> payment.addItem(PaymentItem.of(command.getAmount())))
                .flatMap(paymentRepository::savePayment);
    }

    public Mono<Payment> completePayment(CompletePaymentCommand command) {
        return findPayment(command.getPaymentId())
                .map(Payment::complete)
                .flatMap(paymentRepository::savePayment);
    }

    public Mono<Payment> getPayment(String paymentId) {
        return findPayment(paymentId);
    }

    private Mono<Payment> findPayment(String paymentId) {
        return paymentRepository
                .findById(paymentId)
                .switchIfEmpty(
                        Mono.error(new PaymentNotFoundException("Payment ID Not found :" + paymentId)));
    }
}
