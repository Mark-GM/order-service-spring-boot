package dev.markgm.microservices.order_service.service;

import dev.markgm.microservices.order_service.domain.Order;
import dev.markgm.microservices.order_service.dto.CreateOrderRequest;
import dev.markgm.microservices.order_service.dto.OrderResponse;
import dev.markgm.microservices.order_service.mapper.OrderMapper;
import dev.markgm.microservices.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponse placeOrder(CreateOrderRequest orderRequest) {
        UUID uuid = UUID.randomUUID();
        var order = orderMapper.toOrder(orderRequest, uuid);
        order = orderRepository.save(order);

        return orderMapper.toOrderResponse(order);
    }
}
