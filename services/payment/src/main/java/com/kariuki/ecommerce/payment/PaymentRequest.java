package com.kariuki.ecommerce.payment;

import java.math.BigDecimal;

public record PaymentRequest(
        Integer id,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String orderReference,
        Integer orderId,
        Customer customer
) {
}
