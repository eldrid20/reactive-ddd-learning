package com.xyz.payment;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

public abstract class AbstractContainerBaseTest {

  private static final String DB_DOCKER_IMAGE = "mongo:latest";
  private static MongoDBContainer mongoDBContainer;

  static {
    mongoDBContainer = new MongoDBContainer(DB_DOCKER_IMAGE);
    mongoDBContainer.start();
  }

  @DynamicPropertySource
  static void postgresqlProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mongoDBContainer::getReplicaSetUrl);
  }
}
