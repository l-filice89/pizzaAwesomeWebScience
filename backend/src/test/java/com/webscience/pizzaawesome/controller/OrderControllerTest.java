package com.webscience.pizzaawesome.controller;

import com.webscience.pizzaawesome.model.OrderedPizzas;
import com.webscience.pizzaawesome.request.OrderRequest;
import com.webscience.pizzaawesome.response.OrderDetailKitchenResponse;
import com.webscience.pizzaawesome.response.OrderResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class OrderControllerTest {

    @Autowired
    private OrderController orderController;

    private static OrderRequest orderRequest;

    private static final ModelMapper modelMapper = new ModelMapper();

    @BeforeAll
    public static void init() {
        orderRequest = new OrderRequest();
        orderRequest.setCustomerName("Test Customer");
        orderRequest.setCustomerAddress("Test Address");
        orderRequest.setCustomerPhone("Test Phone");
        orderRequest.setStatus("ordered");

        ArrayList<OrderedPizzas> pizzas = new ArrayList<>();
        pizzas.add(new OrderedPizzas(1, 2));
        pizzas.add(new OrderedPizzas(2, 1));
        pizzas.add(new OrderedPizzas(3, 1));

        orderRequest.setPizzas(pizzas.toArray(new OrderedPizzas[0]));
    }

    @Test
    public void placeOrderTest() {
        ResponseEntity<OrderResponse> responseEntity = orderController.placeOrder(orderRequest);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        OrderResponse orderResponse = responseEntity.getBody();
        assertNotNull(orderResponse);
        assertEquals(orderRequest.getCustomerName(), orderResponse.getCustomerName());
        assertEquals(orderRequest.getCustomerAddress(), orderResponse.getCustomerAddress());
        assertEquals(orderRequest.getCustomerPhone(), orderResponse.getCustomerPhone());
        assertEquals(orderRequest.getStatus(), orderResponse.getStatus());
        orderController.deleteOrderById(orderResponse.getId());
    }

    @Test
    public void testGetOrders() {
        for (int i = 0; i < 5; i++) {
            orderController.placeOrder(orderRequest);
        }
        ResponseEntity<OrderResponse[]> responseEntity = orderController.getOrders();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        OrderResponse[] orderResponses = responseEntity.getBody();
        assertNotNull(orderResponses);
        assertEquals(5, orderResponses.length);
        for (OrderResponse orderResponse : orderResponses) {
            orderController.deleteOrderById(orderResponse.getId());
        }
    }

    @Test
    public void testGetOrderDetailById() {
        ResponseEntity<OrderResponse> responseEntity = orderController.placeOrder(orderRequest);
        assertNotNull(responseEntity.getBody());
        ResponseEntity<OrderDetailKitchenResponse[]> detailResponseEntity = orderController.getOrderDetailById(responseEntity.getBody().getId());
        assertEquals(HttpStatus.OK, detailResponseEntity.getStatusCode());
        OrderDetailKitchenResponse[] orderDetailKitchenResponses = detailResponseEntity.getBody();
        assertNotNull(orderDetailKitchenResponses);
        assertEquals(3, orderDetailKitchenResponses.length);
        orderController.deleteOrderById(responseEntity.getBody().getId());
    }

    @Test
    public void testGetNewOrders() {
        for (int i = 0; i < 5; i++) {
            orderController.placeOrder(orderRequest);
        }
        ResponseEntity<OrderResponse[]> responseEntity = orderController.getNewOrders();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        OrderResponse[] orderResponses = responseEntity.getBody();
        assertNotNull(orderResponses);
        assertEquals(5, orderResponses.length);
        OrderRequest orderRequest = modelMapper.map(orderResponses[0], OrderRequest.class);
        orderRequest.setStatus("cancelled");
        orderController.updateOrderStatus(orderRequest);
        responseEntity = orderController.getNewOrders();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        OrderResponse[] newOrderResponses = responseEntity.getBody();
        assertNotNull(newOrderResponses);
        assertEquals(4, newOrderResponses.length);
        for (OrderResponse orderResponse : orderResponses) {
            orderController.deleteOrderById(orderResponse.getId());
        }
    }

    @Test
    public void testGetOldestOrderId() {
        for (int i = 0; i < 5; i++) {
            orderController.placeOrder(orderRequest);
        }
        ResponseEntity<OrderResponse[]> ordersResponseEntity = orderController.getOrders();
        assertNotNull(ordersResponseEntity.getBody());
        ResponseEntity<Integer> responseEntity = orderController.getOldestOrderId();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(ordersResponseEntity.getBody()[0].getId(), responseEntity.getBody());
        OrderRequest orderRequest = modelMapper.map(ordersResponseEntity.getBody()[0], OrderRequest.class);
        orderRequest.setStatus("cancelled");
        orderController.updateOrderStatus(orderRequest);
        responseEntity = orderController.getOldestOrderId();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(ordersResponseEntity.getBody()[1].getId(), responseEntity.getBody());
        for (OrderResponse orderResponse : ordersResponseEntity.getBody()) {
            orderController.deleteOrderById(orderResponse.getId());
        }
    }

    @Test
    public void testGetOrderById() {
        ResponseEntity<OrderResponse> responseEntity = orderController.placeOrder(orderRequest);
        assertNotNull(responseEntity.getBody());
        ResponseEntity<OrderResponse> orderResponseEntity = orderController.getOrderById(responseEntity.getBody().getId());
        assertEquals(HttpStatus.OK, orderResponseEntity.getStatusCode());
        assertNotNull(orderResponseEntity.getBody());
        assertEquals(responseEntity.getBody().getId(), orderResponseEntity.getBody().getId());
        orderController.deleteOrderById(responseEntity.getBody().getId());
    }

    @Test
    public void testGetOngoingOrders() {
        for (int i = 0; i < 5; i++) {
            orderController.placeOrder(orderRequest);
        }
        ResponseEntity<OrderResponse[]> ordersResponseEntity = orderController.getOrders();
        ResponseEntity<OrderResponse[]> responseEntity = orderController.getOngoingOrders();
        assertNotNull(ordersResponseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(0, responseEntity.getBody().length);
        OrderRequest orderRequest = modelMapper.map(ordersResponseEntity.getBody()[0], OrderRequest.class);
        orderRequest.setStatus("preparing");
        orderController.updateOrderStatus(orderRequest);
        responseEntity = orderController.getOngoingOrders();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1, responseEntity.getBody().length);
        orderRequest.setStatus("delivering");
        orderController.updateOrderStatus(orderRequest);
        orderRequest = modelMapper.map(ordersResponseEntity.getBody()[1], OrderRequest.class);
        orderRequest.setStatus("preparing");
        orderController.updateOrderStatus(orderRequest);
        responseEntity = orderController.getOngoingOrders();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().length);
        for (OrderResponse orderResponse : ordersResponseEntity.getBody()) {
            orderController.deleteOrderById(orderResponse.getId());
        }
    }

    @Test
    public void testGetOrderByStatus(){
        for(int i = 0; i < 5; i++){
            orderController.placeOrder(orderRequest);
        }
        ResponseEntity<OrderResponse[]> ordersResponseEntity = orderController.getOrders();
        assertNotNull(ordersResponseEntity.getBody());
        int n = 1;
        for(OrderResponse orderResponse : ordersResponseEntity.getBody()){
            OrderRequest orderRequest = modelMapper.map(orderResponse, OrderRequest.class);
            orderRequest.setStatus("cancelled");
            orderController.updateOrderStatus(orderRequest);
            ResponseEntity<OrderResponse[]> responseEntity = orderController.getOrderByStatus("cancelled");
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertNotNull(responseEntity.getBody());
            assertEquals(n, responseEntity.getBody().length);
            n++;
        }
        for(OrderResponse orderResponse : ordersResponseEntity.getBody()){
            orderController.deleteOrderById(orderResponse.getId());
        }
    }
}
