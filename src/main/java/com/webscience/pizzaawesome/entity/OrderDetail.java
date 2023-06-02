package com.webscience.pizzaawesome.entity;

import com.webscience.pizzaawesome.model.OrderDetailId;
import jakarta.persistence.*;

@Entity
@Table(name = "order_details")
@IdClass(OrderDetailId.class)
public class OrderDetail {

    @Id
    @Column(name = "order_id")
    int orderId;

    @Id
    @Column(name = "pizza_id")
    int pizzaId;

    @Column(name = "quantity")
    int quantity;

    public OrderDetail() {
    }

    public OrderDetail(int orderId, int pizzaId, int quantity) {
        this.orderId = orderId;
        this.pizzaId = pizzaId;
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(int pizzaId) {
        this.pizzaId = pizzaId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
