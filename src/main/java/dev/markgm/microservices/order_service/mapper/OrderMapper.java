package dev.markgm.microservices.order_service.mapper;

import dev.markgm.microservices.order_service.domain.Order;
import dev.markgm.microservices.order_service.dto.CreateOrderRequest;
import dev.markgm.microservices.order_service.dto.OrderResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderMapper {
    public Order toOrder(CreateOrderRequest createOrderRequest, UUID uuid) {
        return Order.builder()
                .orderNumber(uuid)
                .skuCode(createOrderRequest.skuCode())
                .quantity(createOrderRequest.quantity())
                .price(createOrderRequest.price()).build();
    }

    public OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(
                order.getOrderNumber(),
                order.getSkuCode(),
                order.getPrice(),
                order.getQuantity());
    }
}
