package com.kariuki.ecommerce.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailTemplates {
    PAYMENT_CONFIRMATION("payment-confirmation.html", "Payment Processed Successfully"),
    ORDER_CONFIRMATION("order-confirmation.html", "Order Confirmation");
    private final String template;
    private final String subject;
}
