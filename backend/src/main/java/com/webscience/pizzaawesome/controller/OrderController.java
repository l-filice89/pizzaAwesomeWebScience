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
@CrossOrigin(origins = "http://localhost")
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(value = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest order) {

        OrderResponse orderResponse = orderService.placeOrder(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @GetMapping("")
    public ResponseEntity<OrderResponse[]> getOrders() {

            OrderResponse[] orders = orderService.getOrders();

            return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @GetMapping("/{id}/kitchen")
    public ResponseEntity<OrderDetailKitchenResponse[]> getOrderDetailById(@PathVariable int id) {

            OrderDetailKitchenResponse[] orderDetailKitchen = orderService.getOrderDetailById(id);

            return ResponseEntity.status(HttpStatus.OK).body(orderDetailKitchen);
    }

    @GetMapping("/new")
    public ResponseEntity<OrderResponse[]> getNewOrders() {

            OrderResponse[] orders = orderService.getNewOrders();

            return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @GetMapping("/oldestId")
    public ResponseEntity<Integer> getOldestOrderId() {

            int oldestOrderId = orderService.getOldestOrderId();

            return ResponseEntity.status(HttpStatus.OK).body(oldestOrderId);
    }

    @PutMapping("/updateStatus")
    public ResponseEntity<OrderResponse> updateOrderStatus(@RequestBody OrderRequest order) {

            OrderResponse orderResponse = orderService.updateOrderStatus(order);

            return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
    }

    @GetMapping("/{id}/customer")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable int id) {

            OrderResponse order = orderService.getOrderById(id);

            return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @GetMapping("/ongoing")
    public ResponseEntity<OrderResponse[]> getOngoingOrders() {

            OrderResponse[] orders = orderService.getOngoingOrders();

            return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<OrderResponse[]> getOrderByStatus(@PathVariable String status){

     	OrderResponse[] orders = orderService.getOrdersByStatus(status);

     	return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderById(@PathVariable int id) {

            orderService.deleteOrder(id);

            return ResponseEntity.status(HttpStatus.OK).body("Order n. " + id +  " deleted successfully");
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<String> handleAllExceptions(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

}