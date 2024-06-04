package com.kariuki.ecommerce.product;

import java.math.BigDecimal;

public record PurchaseResponse(
        Integer productId,
        String productName,
        String description,
        BigDecimal price,
        double quantity
) {
}
