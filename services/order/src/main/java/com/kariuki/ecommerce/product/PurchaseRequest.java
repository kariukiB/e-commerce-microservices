package com.kariuki.ecommerce.product;

public record PurchaseRequest(
        Integer productId,
        double quantity
) {
}
