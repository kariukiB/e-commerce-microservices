package com.kariuki.ecommerce.payment;

import com.kariuki.ecommerce.customer.CustomerResponse;
import com.kariuki.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String orderReference,
        Integer orderId,
        CustomerResponse customer
) {
}
