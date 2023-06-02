package com.webscience.pizzaawesome.controller;

import com.webscience.pizzaawesome.request.PizzaRequest;
import com.webscience.pizzaawesome.response.PizzaResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PizzaControllerTest {

    @Autowired
    private PizzaController pizzaController;

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
        ResponseEntity<PizzaResponse[]> startingPizzas = pizzaController.getAllPizzas();
        assertNotNull(startingPizzas.getBody());
        for(int i = 0; i < 5; i++) {
            pizzaController.addPizza(pizzaRequest);
        }
        ResponseEntity<PizzaResponse[]> response = pizzaController.getAllPizzas();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(startingPizzas.getBody().length + 5, response.getBody().length);
        for(PizzaResponse pizzaResponse : response.getBody()) {
            pizzaController.deletePizza(pizzaResponse.getId());
        }
    }

    @Test
    public void testGetPizzaById(){
        ResponseEntity<PizzaResponse> pizzaResponse = pizzaController.addPizza(pizzaRequest);
        assertNotNull(pizzaResponse.getBody());
        ResponseEntity<PizzaResponse> pizzaResponseById = pizzaController.getPizzaById(pizzaResponse.getBody().getId());
        assertEquals(HttpStatus.OK, pizzaResponseById.getStatusCode());
        assertNotNull(pizzaResponseById.getBody());
        assertEquals(pizzaResponseById.getBody().getName(), pizzaRequest.getName());
        assertEquals(pizzaResponseById.getBody().getPrice(), pizzaRequest.getPrice());
        assertEquals(pizzaResponseById.getBody().getIngredients(), pizzaRequest.getIngredients());
        pizzaController.deletePizza(pizzaResponse.getBody().getId());
    }

    @Test
    public void testAddPizza(){
        ResponseEntity<PizzaResponse> pizzaResponse = pizzaController.addPizza(pizzaRequest);
        assertEquals(HttpStatus.CREATED, pizzaResponse.getStatusCode());
        assertNotNull(pizzaResponse.getBody());
        assertEquals(pizzaResponse.getBody().getName(), pizzaRequest.getName());
        assertEquals(pizzaResponse.getBody().getPrice(), pizzaRequest.getPrice());
        assertEquals(pizzaResponse.getBody().getIngredients(), pizzaRequest.getIngredients());
        pizzaController.deletePizza(pizzaResponse.getBody().getId());
    }

}
