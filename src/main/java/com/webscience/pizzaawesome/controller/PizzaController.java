package com.webscience.pizzaawesome.controller;

import com.webscience.pizzaawesome.request.PizzaRequest;
import com.webscience.pizzaawesome.response.PizzaResponse;
import com.webscience.pizzaawesome.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PizzaController {

    private final PizzaService pizzaService;

    @Autowired
    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping("/pizzas")
    public ResponseEntity<PizzaResponse[]> getAllPizzas() {

        PizzaResponse[] pizzas = pizzaService.getPizzas();

        return ResponseEntity.status(HttpStatus.OK).body(pizzas);
    }

    @GetMapping("/pizzas/{id}")
    public ResponseEntity<PizzaResponse> getPizzaById(@PathVariable int id) {

        PizzaResponse pizza = pizzaService.getPizzaById(id);

        return ResponseEntity.status(HttpStatus.OK).body(pizza);
    }

    @PostMapping("/pizzas")
    public ResponseEntity<PizzaResponse> addPizza(@RequestBody PizzaRequest pizza) {

        PizzaResponse pizzaResponse = pizzaService.addPizza(pizza);

        return ResponseEntity.status(HttpStatus.CREATED).body(pizzaResponse);
    }

}
