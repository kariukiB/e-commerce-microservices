package com.kariuki.ecommerce.order;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class BusinessException extends RuntimeException {
    private final String message;
}
