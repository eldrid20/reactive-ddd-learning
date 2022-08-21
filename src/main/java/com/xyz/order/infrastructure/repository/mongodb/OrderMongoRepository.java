package com.xyz.order.infrastructure.repository.mongodb;

import com.xyz.order.domain.model.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMongoRepository extends ReactiveMongoRepository<Order, String> {}
