package com.kariuki.ecommerce.orderline;

import com.kariuki.ecommerce.order.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderLineMapper {
    public OrderLine toOrderLine(OrderLineRequest request){
        return OrderLine.builder()
                .id(request.id())
                .quantity(request.quantity())
                .productId(request.productId())
                .order(Order.builder().id(request.orderId()).build())
                .build();
    }
}
