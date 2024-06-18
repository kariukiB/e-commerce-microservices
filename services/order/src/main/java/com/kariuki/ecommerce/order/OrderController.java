package com.kariuki.ecommerce.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PostMapping
    public ResponseEntity<Integer> createOrder(
            @Valid @RequestBody OrderRequest request
    ) {
       return ResponseEntity.ok(orderService.createOrder(request));

    }

    @GetMapping("/{order-id}")
    public ResponseEntity<Order> getOrderById(@PathVariable("order-id") Integer orderId){
        return ResponseEntity.ok(orderService.findOrder(orderId));
    }


}
