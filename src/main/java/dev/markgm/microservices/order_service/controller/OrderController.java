package dev.markgm.microservices.order_service.controller;

import dev.markgm.microservices.order_service.dto.CreateOrderRequest;
import dev.markgm.microservices.order_service.dto.OrderResponse;
import dev.markgm.microservices.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody CreateOrderRequest createOrderDTO) {
        var orderResponse = orderService.placeOrder(createOrderDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderResponse);
    }
}
