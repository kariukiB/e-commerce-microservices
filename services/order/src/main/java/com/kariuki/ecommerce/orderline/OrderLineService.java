package com.kariuki.ecommerce.orderline;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderLineService {
    private final OrderLineRepository orderLineRepository;
    private final OrderLineMapper mapper;
    public void saveOrderLine(OrderLineRequest orderLineRequest) {
        var order = mapper.toOrderLine(orderLineRequest);
        orderLineRepository.save(order);
    }
}
