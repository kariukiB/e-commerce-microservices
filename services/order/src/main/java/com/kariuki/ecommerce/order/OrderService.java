package com.kariuki.ecommerce.order;

import com.kariuki.ecommerce.customer.CustomerClient;
import com.kariuki.ecommerce.kafka.OrderConfirmation;
import com.kariuki.ecommerce.kafka.OrderProducer;
import com.kariuki.ecommerce.orderline.OrderLineRequest;
import com.kariuki.ecommerce.orderline.OrderLineService;
import com.kariuki.ecommerce.payment.PaymentClient;
import com.kariuki.ecommerce.payment.PaymentRequest;
import com.kariuki.ecommerce.product.ProductClient;
import com.kariuki.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Integer createOrder(OrderRequest request) {
        //Get and validate customer from customer microservice using open feign client
        var customer = this.customerClient.getCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Customer not found for id " + request.customerId()));

        //Make the purchase from purchase microservice using rest template
        var purchasedProducts = this.productClient.purchaseProducts(request.products());
        //Save the order
        var order = this.orderRepository.save(mapper.toOrder(request));
        //Save order line items
        for (PurchaseRequest purchaseRequest : request.products()){
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()

                    )
            );
        }
        //Initiate payment
        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getReference(),
                order.getId(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);
        //Send order to message broker
        orderProducer.sendOrderConfirmation(new OrderConfirmation(
                request.reference(),
                request.amount(),
                request.paymentMethod(),
                customer,
                purchasedProducts
        ));
        return order.getId();
    }

    public Order findOrder(Integer orderId) {
        return orderRepository.findById(orderId).orElseThrow(()-> new EntityNotFoundException(String.format("Order with id: %d not found", orderId)));
    }
}
