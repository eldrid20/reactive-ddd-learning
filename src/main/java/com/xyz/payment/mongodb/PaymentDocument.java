package com.xyz.payment.mongodb;

import com.xyz.payment.domain.model.Payment;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("payments")
public class PaymentDocument extends Payment {
    @Id
    @Override
    public String getId() {
        return super.getId();
    }
}
