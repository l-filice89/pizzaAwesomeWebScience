package com.webscience.pizzaawesome.request;

import com.webscience.pizzaawesome.model.OrderedPizzas;

public class OrderRequest {

    String customerName;
    String customerAddress;
    String customerPhone;
    String status;
    OrderedPizzas[] pizzas;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OrderedPizzas[] getPizzas() {
        return pizzas;
    }

    public void setPizzas(OrderedPizzas[] pizzas) {
        this.pizzas = pizzas;
    }
}
