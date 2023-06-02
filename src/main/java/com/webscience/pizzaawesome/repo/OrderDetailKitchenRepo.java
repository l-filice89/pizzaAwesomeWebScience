package com.webscience.pizzaawesome.repo;

import com.webscience.pizzaawesome.entity.OrderDetailKitchen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailKitchenRepo extends JpaRepository<OrderDetailKitchen, String>{

        @Query(nativeQuery = true, value = "SELECT p.name as pizza, p.ingredients as ingredients, od.quantity as quantity FROM pizzas p JOIN order_details od on p.id = od.pizza_id JOIN orders o on od.order_id = o.id WHERE order_id=:orderId")
        OrderDetailKitchen[] getOrderDetailsForKitchen(@Param("orderId") int id);

}
