# Domain Driven Design(DDD) Sample

This repository shows simple project of Payment using DDD Aggregate design and Hexagonal Architecture

Package definition

The packages structure follows hexagonal architecture pattern:

* `com.xyz.payment.adapter.incoming.rest` => This package contains Adapter code including DTOs and Spring Rest
  Controller.

* `com.xyz.payment.application` => This package contains codes to glue adapter and domain logic.

* `com.xyz.payment.domain` => This package contains domain objects and business logic with DDD Aggregate pattern.

* `com.xyz.payment.infrastructure` => This package contains infrastructure code (ie: Database, Messaging,etc).

# Adapter

## REST

Adapter implementation will support incoming request from REST service

### API Specification

API Specification follows [OpenAPI Specification](https://swagger.io/specification/) and specification defined in
[API Spec](src/main/resources/api-spec/payment-api.yaml)