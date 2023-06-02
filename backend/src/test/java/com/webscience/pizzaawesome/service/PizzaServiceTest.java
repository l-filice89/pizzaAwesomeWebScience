package com.webscience.pizzaawesome.service;

import com.webscience.pizzaawesome.request.PizzaRequest;
import com.webscience.pizzaawesome.response.PizzaResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PizzaServiceTest {

    @Autowired
    private PizzaService pizzaService;

    private static PizzaRequest pizzaRequest;

    @BeforeAll
    public static void init() {
        pizzaRequest = new PizzaRequest();
        pizzaRequest.setName("Test Pizza");
        pizzaRequest.setPrice(10.0);
        pizzaRequest.setIngredients("Test Ingredient 1, Test Ingredient 2");
    }

    @Test
    public void testGetPizzas() {
        pizzaService.getPizzas();
        PizzaResponse[] startingPizzas = pizzaService.getPizzas();
        PizzaResponse pizzaResponse = pizzaService.addPizza(pizzaRequest);
        PizzaResponse[] endingPizzas = pizzaService.getPizzas();
        assert (endingPizzas.length == startingPizzas.length + 1);
        pizzaService.deletePizza(pizzaResponse.getId());
    }

    @Test
    public void testAddPizzas(){
        PizzaResponse pizzaResponse = pizzaService.addPizza(pizzaRequest);
        assert (pizzaResponse.getName().equals(pizzaRequest.getName()));
        assert (pizzaResponse.getPrice().equals(pizzaRequest.getPrice()));
        assert (pizzaResponse.getIngredients().equals(pizzaRequest.getIngredients()));
        pizzaService.deletePizza(pizzaResponse.getId());
    }

    @Test
    public void testGetPizzaById(){
        PizzaResponse pizzaResponse = pizzaService.addPizza(pizzaRequest);
        PizzaResponse pizzaResponseById = pizzaService.getPizzaById(pizzaResponse.getId());
        assert (pizzaResponseById.getName().equals(pizzaRequest.getName()));
        assert (pizzaResponseById.getPrice().equals(pizzaRequest.getPrice()));
        assert (pizzaResponseById.getIngredients().equals(pizzaRequest.getIngredients()));
        pizzaService.deletePizza(pizzaResponse.getId());
    }

}
