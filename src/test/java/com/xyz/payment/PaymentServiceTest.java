package com.xyz.payment;

import com.xyz.payment.application.PaymentService;
import com.xyz.payment.domain.command.AddPaymentItemCommand;
import com.xyz.payment.domain.command.CompletePaymentCommand;
import com.xyz.payment.domain.command.CreatePaymentCommand;
import com.xyz.payment.domain.exception.PaymentNotFoundException;
import com.xyz.payment.domain.model.Payment;
import com.xyz.payment.domain.model.PaymentStatus;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class PaymentServiceTest extends AbstractContainerBaseTest {

  @Autowired private PaymentService paymentService;

  @Test
  void createPaymentByCommand_ShouldCreatedSuccessfully() {
    // given
    final var command = CreatePaymentCommand.of(BigDecimal.valueOf(10.00));

    // when
    final var payment = paymentService.createPayment(command).block();

    // then
    assert payment != null;
    assertThat(payment.getTotalAmount().compareTo(BigDecimal.valueOf(10.00))).isZero();
  }

  @Test
  void addPaymentItemByCommand_ShouldBeAddedSuccessfully() {
    // given
    final var createPaymentCommand = CreatePaymentCommand.of(BigDecimal.valueOf(10.00));
    paymentService
        .createPayment(createPaymentCommand)
        // when
        .flatMap(
            payment -> {
              final var addItemCommand =
                  AddPaymentItemCommand.of(payment.getId(), BigDecimal.valueOf(35.00));
              return paymentService.addPaymentItem(addItemCommand);
            })
        // then
        .doOnSuccess(payment -> verifyTotalPaymentItem(payment, BigDecimal.valueOf(45.00)))
        .subscribe();
  }

  @Test
  void addPaymentItemByCommand_WhenInvalidPaymentIdSupplied_ShouldThrowPaymentNotFoundException() {
    // given
    final var addPaymentItemCommand = AddPaymentItemCommand.of("-1", BigDecimal.valueOf(12));
    // when & then
    StepVerifier.create(paymentService.addPaymentItem(addPaymentItemCommand))
        .expectError(PaymentNotFoundException.class)
        .verify();
  }

  @Test
  void completePaymentByCommand_ShouldBeProceedSuccessfully() {
    // given
    final var createPaymentCommand = CreatePaymentCommand.of(BigDecimal.valueOf(10.00));
    paymentService
        .createPayment(createPaymentCommand)
        // when
        .flatMap(
            updatedPayment -> {
              final var completeCommand = CompletePaymentCommand.of(updatedPayment.getId());
              return paymentService.completePayment(completeCommand);
            })
        // then
        .doOnSuccess(this::verifyPaymentCompleted)
        .subscribe();
  }

  @NotNull
  private Mono<Payment> verifyPaymentCompleted(Payment payment) {
    return paymentService
        .getPayment(payment.getId())
        .doOnSuccess(
            payment1 -> assertThat(payment1.getStatus()).isEqualTo(PaymentStatus.COMPLETED));
  }

  @NotNull
  private Mono<Payment> verifyTotalPaymentItem(Payment payment, BigDecimal totalAmount) {
    return paymentService
        .getPayment(payment.getId())
        .doOnSuccess(
            updatedPayment ->
                assertThat(updatedPayment.getTotalAmount().compareTo(totalAmount)).isEqualTo(0));
  }

  @Test
  void completePaymentByCommand_WhenInvalidPaymentIdSupplied_ShouldThrowPaymentNotFoundException() {
    // given
    final var completeCommand = CompletePaymentCommand.of("-1");
    // when & then
    StepVerifier.create(paymentService.completePayment(completeCommand))
        .expectError(PaymentNotFoundException.class)
        .verify();
  }
}
