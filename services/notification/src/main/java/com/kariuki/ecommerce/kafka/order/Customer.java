package com.kariuki.ecommerce.kafka.order;

public record Customer(
        String id,
        String firstName,
        String lastName,
        String email
) {
}
