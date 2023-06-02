package com.webscience.pizzaawesome.service;

import com.webscience.pizzaawesome.model.OrderedPizzas;
import com.webscience.pizzaawesome.request.OrderRequest;
import com.webscience.pizzaawesome.response.OrderDetailKitchenResponse;
import com.webscience.pizzaawesome.response.OrderResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    private final ModelMapper modelMapper = new ModelMapper();

    private static OrderRequest orderRequest;

    @BeforeAll
    public static void initOrderService() {
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
    void testOrderCreation() {
        OrderResponse orderResponse = orderService.placeOrder(orderRequest);
        assertNotNull(orderResponse);
        assertEquals("Test Customer", orderResponse.getCustomerName());
        assertEquals("Test Address", orderResponse.getCustomerAddress());
        assertEquals("Test Phone", orderResponse.getCustomerPhone());
        assertEquals("ordered", orderResponse.getStatus());

        OrderDetailKitchenResponse[] orderDetails = orderService.getOrderDetailById(orderResponse.getId());
        assertEquals(3, orderDetails.length);

        orderService.deleteOrder(orderResponse.getId());
    }

    @Test
    void testFailedCreation() {
        orderRequest.getPizzas()[0].setPizzaId(100);
        OrderResponse orderResponse = orderService.placeOrder(orderRequest);
        assertNotNull(orderResponse);
        assertEquals("cancelled", orderResponse.getStatus());
        assertEquals(0, orderService.getOrderDetailById(orderResponse.getId()).length);
        orderService.deleteOrder(orderResponse.getId());
        orderRequest.getPizzas()[0].setPizzaId(1);
    }

    @Test
    void testGetOrders() {
        for (int i = 0; i < 3; i++) {
            orderService.placeOrder(orderRequest);
        }
        OrderResponse[] orderResponses = orderService.getOrders();
        assertNotNull(orderResponses);
        assertEquals(3, orderResponses.length);
        for (OrderResponse orderResponse : orderResponses) {
            orderService.deleteOrder(orderResponse.getId());
        }
    }

    @Test
    void testGetOrderDetailById() {
        OrderResponse orderResponse = orderService.placeOrder(orderRequest);
        OrderDetailKitchenResponse[] orderDetailKitchenResponses = orderService.getOrderDetailById(orderResponse.getId());
        assertEquals(3, orderDetailKitchenResponses.length);
        assertEquals(2, orderDetailKitchenResponses[0].getQuantity());
        assertEquals(1, orderDetailKitchenResponses[1].getQuantity());
        assertEquals(1, orderDetailKitchenResponses[2].getQuantity());
        orderService.deleteOrder(orderResponse.getId());
    }

    @Test
    void testGetNewOrders() {
        for (int i = 0; i < 3; i++) {
            orderService.placeOrder(orderRequest);
        }
        OrderResponse[] orderResponses = orderService.getNewOrders();
        assertNotNull(orderResponses);
        assertEquals(3, orderResponses.length);
        OrderRequest updateRequest = modelMapper.map(orderResponses[0], OrderRequest.class);
        updateRequest.setStatus("cancelled");
        orderService.updateOrderStatus(updateRequest);
        OrderResponse[] newOrderResponses = orderService.getNewOrders();
        assertNotNull(newOrderResponses);
        assertEquals(2, newOrderResponses.length);
        for (OrderResponse orderResponse : orderResponses) {
            orderService.deleteOrder(orderResponse.getId());
        }
    }

    @Test
    void testGetOldestOrderId() {
        for (int i = 0; i < 3; i++) {
            orderService.placeOrder(orderRequest);
        }
        OrderResponse[] orderResponses = orderService.getOrders();
        int oldestOrder = orderService.getOldestOrderId();
        assertNotNull(orderResponses);
        assertEquals(orderResponses[0].getId(), oldestOrder);
        OrderRequest updateRequest = modelMapper.map(orderResponses[0], OrderRequest.class);
        updateRequest.setStatus("cancelled");
        orderService.updateOrderStatus(updateRequest);
        oldestOrder = orderService.getOldestOrderId();
        assertEquals(orderResponses[1].getId(), oldestOrder);
        for (OrderResponse orderResponse : orderResponses) {
            orderService.deleteOrder(orderResponse.getId());
        }
    }

    @Test
    void testUpdateWithMultipleOrdersInPreparation() {
        for (int i = 0; i < 3; i++) {
            orderService.placeOrder(orderRequest);
        }
        OrderResponse[] orderResponses = orderService.getOrders();
        OrderRequest updateRequest = modelMapper.map(orderResponses[0], OrderRequest.class);
        updateRequest.setStatus("preparing");
        OrderResponse updatedOrder = orderService.updateOrderStatus(updateRequest);
        assertNotNull(updatedOrder);
        assertEquals("preparing", updatedOrder.getStatus());
        OrderRequest illegalUpdateRequest = modelMapper.map(orderResponses[1], OrderRequest.class);
        updateRequest.setStatus("preparing");
        assertThrows(IllegalArgumentException.class, () -> orderService.updateOrderStatus(illegalUpdateRequest));
        for (OrderResponse orderResponse : orderResponses) {
            orderService.deleteOrder(orderResponse.getId());
        }

    }

    @Test
    void testCheckStatusFlow() {
        OrderResponse orderResponse = orderService.placeOrder(orderRequest);
        assertEquals("ordered", orderResponse.getStatus());
        OrderRequest updateRequest = modelMapper.map(orderResponse, OrderRequest.class);
        updateRequest.setStatus("preparing");
        OrderResponse updatedOrder = orderService.updateOrderStatus(updateRequest);
        assertEquals("preparing", updatedOrder.getStatus());
        updateRequest = modelMapper.map(updatedOrder, OrderRequest.class);
        updateRequest.setStatus("delivering");
        updatedOrder = orderService.updateOrderStatus(updateRequest);
        assertEquals("delivering", updatedOrder.getStatus());
        updateRequest = modelMapper.map(updatedOrder, OrderRequest.class);
        updateRequest.setStatus("delivered");
        updatedOrder = orderService.updateOrderStatus(updateRequest);
        assertEquals("delivered", updatedOrder.getStatus());
        orderService.deleteOrder(orderResponse.getId());

        orderResponse = orderService.placeOrder(orderRequest);
        assertEquals("ordered", orderResponse.getStatus());
        updateRequest = modelMapper.map(orderResponse, OrderRequest.class);
        updateRequest.setStatus("cancelled");
        updatedOrder = orderService.updateOrderStatus(updateRequest);
        assertEquals("cancelled", updatedOrder.getStatus());
        orderService.deleteOrder(orderResponse.getId());

        orderResponse = orderService.placeOrder(orderRequest);
        assertEquals("ordered", orderResponse.getStatus());
        updateRequest = modelMapper.map(orderResponse, OrderRequest.class);
        updateRequest.setStatus("delivering");
        OrderRequest finalUpdateRequest = updateRequest;
        assertThrows(IllegalArgumentException.class, () -> orderService.updateOrderStatus(finalUpdateRequest));
        orderService.deleteOrder(orderResponse.getId());

        orderResponse = orderService.placeOrder(orderRequest);
        assertEquals("ordered", orderResponse.getStatus());
        updateRequest = modelMapper.map(orderResponse, OrderRequest.class);
        updateRequest.setStatus("preparing");
        updatedOrder = orderService.updateOrderStatus(updateRequest);
        assertEquals("preparing", updatedOrder.getStatus());
        updateRequest = modelMapper.map(updatedOrder, OrderRequest.class);
        updateRequest.setStatus("cancelled");
        OrderRequest finalUpdateRequest1 = updateRequest;
        assertThrows(IllegalArgumentException.class, () -> orderService.updateOrderStatus(finalUpdateRequest1));
        orderService.deleteOrder(orderResponse.getId());

        orderResponse = orderService.placeOrder(orderRequest);
        assertEquals("ordered", orderResponse.getStatus());
        updateRequest = modelMapper.map(orderResponse, OrderRequest.class);
        updateRequest.setStatus("preparing");
        updatedOrder = orderService.updateOrderStatus(updateRequest);
        assertEquals("preparing", updatedOrder.getStatus());
        updateRequest = modelMapper.map(updatedOrder, OrderRequest.class);
        updateRequest.setStatus("delivering");
        updatedOrder = orderService.updateOrderStatus(updateRequest);
        assertEquals("delivering", updatedOrder.getStatus());
        updateRequest = modelMapper.map(updatedOrder, OrderRequest.class);
        updateRequest.setStatus("cancelled");
        OrderRequest finalUpdateRequest2 = updateRequest;
        assertThrows(IllegalArgumentException.class, () -> orderService.updateOrderStatus(finalUpdateRequest2));
        orderService.deleteOrder(orderResponse.getId());

        orderResponse = orderService.placeOrder(orderRequest);
        assertEquals("ordered", orderResponse.getStatus());
        updateRequest = modelMapper.map(orderResponse, OrderRequest.class);
        updateRequest.setStatus("cancelled");
        updatedOrder = orderService.updateOrderStatus(updateRequest);
        assertEquals("cancelled", updatedOrder.getStatus());
        updateRequest = modelMapper.map(updatedOrder, OrderRequest.class);
        updateRequest.setStatus("preparing");
        OrderRequest finalUpdateRequest3 = updateRequest;
        assertThrows(IllegalArgumentException.class, () -> orderService.updateOrderStatus(finalUpdateRequest3));
        orderService.deleteOrder(orderResponse.getId());
    }
}
