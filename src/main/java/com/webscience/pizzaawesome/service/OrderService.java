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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class OrderService {

    private final OrderRepo orderRepo;
    private final ModelMapper modelMapper;
    private final OrderDetailsRepo orderDetailsRepo;
    private final OrderDetailKitchenRepo orderDetailKitchenRepo;

    @Autowired
    public OrderService(OrderRepo orderRepo, ModelMapper modelMapper, OrderDetailsRepo orderDetailsRepo, OrderDetailKitchenRepo orderDetailKitchenRepo) {
        this.orderRepo = orderRepo;
        this.modelMapper = modelMapper;
        this.orderDetailsRepo = orderDetailsRepo;
        this.orderDetailKitchenRepo = orderDetailKitchenRepo;
    }


    public OrderResponse placeOrder(OrderRequest order) {
        Order orderEntity = modelMapper.map(order, Order.class);

        OrderedPizzas[] orderedPizzas = order.getPizzas();

        orderEntity = orderRepo.save(orderEntity);

        int orderId = orderEntity.getId();

        for (OrderedPizzas orederPizza : orderedPizzas) {
            OrderDetail orderDetail = new OrderDetail(orderId, orederPizza.getPizzaId(), orederPizza.getQuantity());
            try {
                System.out.println("OrderId" + orderId);
                orderDetailsRepo.save(orderDetail);
            } catch (Exception e) {
                System.out.println("In this case set order to cancelled");
                e.printStackTrace();
            }
        }

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
}
