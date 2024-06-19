package com.kariuki.ecommerce.kafka;

import com.kariuki.ecommerce.email.EmailService;
import com.kariuki.ecommerce.kafka.order.OrderConfirmation;
import com.kariuki.ecommerce.kafka.payment.PaymentConfirmation;
import com.kariuki.ecommerce.notification.Notification;
import com.kariuki.ecommerce.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.kariuki.ecommerce.notification.NotificationType.ORDER_CONFIRMATION;
import static com.kariuki.ecommerce.notification.NotificationType.PAYMENT_CONFIRMATION;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotification(PaymentConfirmation paymentConfirmation){
        log.info("Payment confirmation: {}", paymentConfirmation);
        //Create and persist the notification
        notificationRepository.save(Notification.builder()
                        .paymentConfirmation(paymentConfirmation)
                        .type(PAYMENT_CONFIRMATION)
                .build());
        try {
            emailService.sendPaymentSuccessEmail(
                    paymentConfirmation.customerEmail(),
                    paymentConfirmation.customerFirstName() + " " + paymentConfirmation.customerLastName(),
                    paymentConfirmation.orderReference(),
                    paymentConfirmation.amount()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @KafkaListener(topics = "order-topic")
    public void consumeOrderSuccessNotification(OrderConfirmation orderConfirmation){
        log.info("Order confirmation: {}", orderConfirmation);
        //Create and persist the notification
        notificationRepository.save(Notification.builder()
                .orderConfirmation(orderConfirmation)
                .type(ORDER_CONFIRMATION)
                .build());

        try {
            emailService.sendOrderConfirmationEmail(
                    orderConfirmation.customer().email(),
                    orderConfirmation.customer().firstName() + " " + orderConfirmation.customer().lastName(),
                    orderConfirmation.orderReference(),
                    orderConfirmation.totalAmount(),
                    orderConfirmation.products()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
