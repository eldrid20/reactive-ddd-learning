package com.xyz.payment.domain.exception;

public class InvalidPaymentStateException extends RuntimeException {
  public InvalidPaymentStateException(String message) {
    super(message);
  }
}
