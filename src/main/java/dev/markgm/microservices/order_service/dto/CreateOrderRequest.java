package dev.markgm.microservices.order_service.dto;

import java.math.BigDecimal;

public record CreateOrderRequest(String skuCode,
                                 BigDecimal price,
                                 Integer quantity) {
}
