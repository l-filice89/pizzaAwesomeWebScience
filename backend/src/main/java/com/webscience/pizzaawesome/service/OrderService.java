package com.webscience.pizzaawesome.service;

import com.webscience.pizzaawesome.entity.Order;
import com.webscience.pizzaawesome.entity.OrderDetail;
import com.webscience.pizzaawesome.entity.OrderDetailKitchen;
import com.webscience.pizzaawesome.model.OrderedPizzas;
import com.webscience.pizzaawesome.repo.OrderDetailKitchenRepo;
import com.webscience.pizzaawesome.repo.OrderDetailsRepo;
import com.webscience.pizzaawesome.repo.OrderRepo;
import com.webscience.pizzaawesome.request.OrderRequest;
import com.webscience.pizzaawesome.response.OrderDetailKitchenResponse;
import com.webscience.pizzaawesome.response.OrderResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service
public class OrderService {

    private final OrderRepo orderRepo;
    private final ModelMapper modelMapper;
    private final OrderDetailsRepo orderDetailsRepo;
    private final OrderDetailKitchenRepo orderDetailKitchenRepo;

    public OrderService(OrderRepo orderRepo, ModelMapper modelMapper, OrderDetailsRepo orderDetailsRepo, OrderDetailKitchenRepo orderDetailKitchenRepo) {
        this.orderRepo = orderRepo;
        this.modelMapper = modelMapper;
        this.orderDetailsRepo = orderDetailsRepo;
        this.orderDetailKitchenRepo = orderDetailKitchenRepo;
    }

    private final ExampleMatcher statusExampleMatcher = ExampleMatcher.matchingAny()
            .withMatcher("status", ExampleMatcher.GenericPropertyMatchers.exact().ignoreCase())
            .withIgnorePaths("id", "customerName", "customerAddress", "customerPhone");


    public OrderResponse placeOrder(OrderRequest order) {
        Order orderEntity = modelMapper.map(order, Order.class);

        OrderedPizzas[] orderedPizzas = order.getPizzas();

        orderEntity = orderRepo.save(orderEntity);

        int orderId = orderEntity.getId();

        for (OrderedPizzas orederPizza : orderedPizzas) {
            OrderDetail orderDetail = new OrderDetail(orderId, orederPizza.getPizzaId(), orederPizza.getQuantity());
            try {
                orderDetailsRepo.save(orderDetail);
            } catch (Exception e) {
                orderEntity.setStatus("cancelled");
                orderRepo.save(orderEntity);
                orderDetailsRepo.deleteByOrderId(orderId);
                break;
            }}
        return modelMapper.map(orderEntity, OrderResponse.class);
    }

    public OrderResponse[] getOrders() {

        Order[] orders = orderRepo.findAll().toArray(new Order[0]);

        return modelMapper.map(orders, OrderResponse[].class);
    }
    public OrderDetailKitchenResponse[] getOrderDetailById(int id) {

        OrderDetailKitchen[] orderDetailsKitchen = orderDetailKitchenRepo.getOrderDetailsForKitchen(id);

        ArrayList<OrderDetailKitchenResponse> orderDetailsKitchenResponses = new ArrayList<>();

        for (OrderDetailKitchen orderDetailKitchen : orderDetailsKitchen) {
            orderDetailsKitchenResponses.add(modelMapper.map(orderDetailKitchen, OrderDetailKitchenResponse.class));
        }
        return orderDetailsKitchenResponses.toArray(new OrderDetailKitchenResponse[0]);
    }

    public OrderResponse[] getNewOrders() {

        Example<Order> example = generateStatusExample("ordered");

        Order[] orders = orderRepo.findAll(example).toArray(new Order[0]);

        return modelMapper.map(orders, OrderResponse[].class);
    }
    public int getOldestOrderId() {

        ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("status", ExampleMatcher.GenericPropertyMatchers.exact().ignoreCase())
                .withIgnorePaths("id", "customerName", "customerAddress", "customerPhone");
        Order sampleOrder = new Order();
        sampleOrder.setStatus("ordered");
        Example<Order> example = Example.of(sampleOrder, customExampleMatcher);
        return orderRepo.findAll(example, Sort.by(Sort.Direction.ASC, "id")).get(0).getId();
    }

    public OrderResponse updateOrderStatus(OrderRequest order) throws NoSuchElementException, IllegalArgumentException {
        Order oldOrder = orderRepo.findById(order.getId()).get();

        checkStatusFlow(oldOrder.getStatus(), order.getStatus());

        if (order.getStatus().equals("preparing")) {
            checkIfNotMakingOtherPizzas();
        }
        oldOrder.setStatus(order.getStatus());
        Order updatedOrder = orderRepo.save(oldOrder);

        return modelMapper.map(updatedOrder, OrderResponse.class);
    }

    private void checkIfNotMakingOtherPizzas() {
        Example<Order> example = generateStatusExample("preparing");

        if (orderRepo.findAll(example).toArray(new Order[0]).length > 0) {
            throw new IllegalArgumentException("Another order is being prepared");
        }}
    private Example<Order> generateStatusExample(String status) {
        Order sampleOrder = new Order();
        sampleOrder.setStatus(status);
        return Example.of(sampleOrder, statusExampleMatcher);
    }

    private void checkStatusFlow(String oldStatus, String newStatus) {
        switch (oldStatus) {
            case "ordered" -> {
                if (!newStatus.equals("cancelled") && !newStatus.equals("preparing")) {
                    throw new IllegalArgumentException("Invalid status change");
                }}
            case "preparing" -> {
                if (!newStatus.equals("delivering")) {
                    throw new IllegalArgumentException("Invalid status change");
                }}
            case "delivering" -> {
                if (!newStatus.equals("delivered")) {
                    throw new IllegalArgumentException("Invalid status change");
                }}
            default -> throw new IllegalArgumentException("Invalid status change");
        }}
    public OrderResponse getOrderById(int id) {
        Order order = orderRepo.findById(id).get();
        return modelMapper.map(order, OrderResponse.class);
    }
    public OrderResponse[] getOngoingOrders() {
        Order[] orders = orderRepo.getOngoingOrders();
        return modelMapper.map(orders, OrderResponse[].class);
    }
    public OrderResponse[] getQueryByStatus(String status) {
        Example<Order> example = generateStatusExample(status);
        Order[] orders = orderRepo.findAll(example).toArray(new Order[0]);
        return modelMapper.map(orders, OrderResponse[].class);
    }
    public boolean deleteOrder(int id) {
        try {
            orderRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}