package com.kariuki.ecommerce.product;

public record ProductPurchaseRequest(
        Integer productId,
        double quantity
) {
}
