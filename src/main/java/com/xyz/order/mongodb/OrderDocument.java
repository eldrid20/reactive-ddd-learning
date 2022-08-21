package com.xyz.order.mongodb;

import com.xyz.order.domain.model.Order;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("orders")
public class OrderDocument extends Order {
    @Id
    @Override
    public String getId() {
        return super.getId();
    }
}
