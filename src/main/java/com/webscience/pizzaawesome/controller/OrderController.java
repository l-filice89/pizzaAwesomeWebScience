package com.webscience.pizzaawesome.controller;

import com.webscience.pizzaawesome.request.OrderRequest;
import com.webscience.pizzaawesome.response.OrderDetailKitchenResponse;
import com.webscience.pizzaawesome.response.OrderResponse;
import com.webscience.pizzaawesome.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(value = "/orders", consumes = "application/json", produces = "application/json")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest order) {

        OrderResponse orderResponse = orderService.placeOrder(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @GetMapping("/orders")
    public ResponseEntity<OrderResponse[]> getOrders() {

            OrderResponse[] orders = orderService.getOrders();

            return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @GetMapping("/orders/{id}/kitchen")
    public ResponseEntity<OrderDetailKitchenResponse[]> getOrderDetailById(@PathVariable int id) {

            OrderDetailKitchenResponse[] orderDetailKitchen = orderService.getOrderDetailById(id);

            return ResponseEntity.status(HttpStatus.OK).body(orderDetailKitchen);
    }

}