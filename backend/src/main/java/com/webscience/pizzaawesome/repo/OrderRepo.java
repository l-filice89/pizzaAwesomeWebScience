package com.webscience.pizzaawesome.repo;

import com.webscience.pizzaawesome.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM orders WHERE status IN ('preparing', 'delivering')")
    Order[] getOngoingOrders();
}
