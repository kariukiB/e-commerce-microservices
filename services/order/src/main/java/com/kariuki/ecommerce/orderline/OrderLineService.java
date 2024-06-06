package com.kariuki.ecommerce.orderline;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderLineService {
    private final OrderLineRepository orderLineRepository;
    private final OrderLineMapper mapper;
    public void saveOrderLine(OrderLineRequest orderLineRequest) {
        var order = mapper.toOrderLine(orderLineRequest);
        orderLineRepository.save(order);
    }

    public List<OrderLine> findLinesByOrder(Integer orderId) {
        return orderLineRepository.findAllByOrderId(orderId);
    }
}
