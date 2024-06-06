package com.kariuki.ecommerce.payment;

import com.kariuki.ecommerce.notification.NotificationProducer;
import com.kariuki.ecommerce.notification.PaymentNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository repository;
    private final NotificationProducer notificationProducer;

    public Integer startPayment(PaymentRequest request) {
        var payment = Payment.builder()
                .id(request.id())
                .paymentMethod(request.paymentMethod())
                .orderId(request.orderId())
                .amount(request.amount())
                .build();
        repository.save(payment);
        notificationProducer.sendNotification(new PaymentNotificationRequest(
                request.orderReference(),
                request.amount(),
                request.paymentMethod(),
                request.customer().firstName(),
                request.customer().lastName(),
                request.customer().email()
        ));
        return payment.getId();
    }
}
