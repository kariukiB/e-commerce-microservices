package com.kariuki.ecommerce.kafka;

import com.kariuki.ecommerce.customer.CustomerResponse;
import com.kariuki.ecommerce.order.PaymentMethod;
import com.kariuki.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
