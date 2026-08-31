package dev.markgm.microservices.order_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderResponse(UUID orderNumber,
                            String skuCode,
                            BigDecimal price,
                            Integer quantity) {
}
