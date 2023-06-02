package com.webscience.pizzaawesome.service;

import com.webscience.pizzaawesome.entity.Pizza;
import com.webscience.pizzaawesome.repo.PizzaRepo;
import com.webscience.pizzaawesome.request.PizzaRequest;
import com.webscience.pizzaawesome.response.PizzaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;

@Service
public class PizzaService {

    private final PizzaRepo pizzaRepo;

    private final ModelMapper modelMapper;

    @Autowired
    public PizzaService(PizzaRepo pizzaRepo, ModelMapper modelMapper) {
        this.pizzaRepo = pizzaRepo;
        this.modelMapper = modelMapper;
    }

    public PizzaResponse[] getPizzas() {

        Pizza[] pizzas = pizzaRepo.findAll().toArray(new Pizza[0]);

        ArrayList<PizzaResponse> pizzaResponses = new ArrayList<>();

        for (Pizza pizza : pizzas) {
            pizzaResponses.add(modelMapper.map(pizza, PizzaResponse.class));
        }

        return pizzaResponses.toArray(new PizzaResponse[0]);
    }

    public PizzaResponse getPizzaById(int id) {

        Pizza pizza = pizzaRepo.findById(id).orElse(null);
        if (pizza == null) {
            return null;
        }
        return modelMapper.map(pizza, PizzaResponse.class);
    }

    public PizzaResponse addPizza(PizzaRequest pizza) {

        Pizza pizzaEntity = modelMapper.map(pizza, Pizza.class);
        pizzaEntity = pizzaRepo.save(pizzaEntity);

        return modelMapper.map(pizzaEntity, PizzaResponse.class);
    }
}
