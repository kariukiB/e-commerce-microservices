package com.kariuki.ecommerce.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-client",
        url = "http://localhost:8222/api/v1/payments"
)
public interface PaymentClient {
    @PostMapping
    Integer requestOrderPayment(@RequestBody PaymentRequest request);
}
