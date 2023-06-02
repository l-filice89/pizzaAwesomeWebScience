package com.webscience.pizzaawesome;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application_test.properties")
@SpringBootTest
class PizzaAwesomeApplicationTests {

    @Test
    void contextLoads() {
    }

}
