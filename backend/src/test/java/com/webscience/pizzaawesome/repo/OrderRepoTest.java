package com.webscience.pizzaawesome.repo;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class OrderRepoTest {

    @Autowired
    OrderRepo orderRepo;

    @Test
    void testGetOngoingOrders(){

        assertEquals(0, orderRepo.getOngoingOrders().length);
    }


}
