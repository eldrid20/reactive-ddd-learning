package com.xyz.payment.adapter.incoming.rest;

import com.xyz.payment.adapter.incoming.rest.dto.PaymentDto;
import com.xyz.payment.application.PaymentService;
import com.xyz.payment.domain.command.AddPaymentItemCommand;
import com.xyz.payment.domain.command.CompletePaymentCommand;
import com.xyz.payment.domain.command.CreatePaymentCommand;
import com.xyz.payment.domain.model.Payment;
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
public class PaymentController {

  private final PaymentService paymentService;

  @PostMapping(
      value = "/v1/payments",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Payment> createPayment(@RequestBody @Valid PaymentDto paymentDto) {
    return paymentService.createPayment(CreatePaymentCommand.of(paymentDto.getAmount()));
  }

  @PutMapping(
      value = "/v1/payments/{paymentId}/items",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Payment> addPaymentItem(
      @PathVariable String paymentId, @RequestBody @Valid PaymentDto paymentDto) {
    return paymentService.addPaymentItem(
        AddPaymentItemCommand.of(paymentId, paymentDto.getAmount()));
  }

  @PutMapping(
      value = "/v1/payments/{paymentId}/complete",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Payment> completePaymentItem(@PathVariable String paymentId) {
    return paymentService.completePayment(CompletePaymentCommand.of(paymentId));
  }
}
