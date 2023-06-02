package com.webscience.pizzaawesome.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Subselect;

@Entity
@Subselect("SELECT p.name as pizza, p.ingredients as ingredients, od.quantity as quantity FROM pizzas p JOIN order_details od on p.id = od.pizza_id JOIN orders o on od.order_id = o.id WHERE order_id=:orderId")
public class OrderDetailKitchen {

    @Id
    String pizza;
    String ingredients;
    int quantity;

    public String getPizza() {
        return pizza;
    }

    public void setPizza(String pizza) {
        this.pizza = pizza;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
