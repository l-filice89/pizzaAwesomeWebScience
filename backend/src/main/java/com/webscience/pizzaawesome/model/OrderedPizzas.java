package com.webscience.pizzaawesome.model;

public class OrderedPizzas {

    private int pizzaId;
    private int quantity;

    public OrderedPizzas(int pizzaId, int quantity) {
        this.pizzaId = pizzaId;
        this.quantity = quantity;
    }

    public OrderedPizzas() {
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
