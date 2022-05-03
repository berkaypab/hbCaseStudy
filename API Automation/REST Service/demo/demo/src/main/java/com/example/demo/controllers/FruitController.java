package com.example.demo.controllers;

import com.example.demo.entities.Fruit;
import com.example.demo.exceptions.FruitNotFoundException;
import com.example.demo.requests.FruitCreateRequest;
import com.example.demo.requests.FruitUpdateRequest;
import com.example.demo.responses.FruitResponse;
import com.example.demo.services.FruitService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/allGrocery")
public class FruitController {


    FruitService fruitService;

    FruitController(FruitService fruitService) {

        this.fruitService = fruitService;
    }

    @GetMapping
    public List<Fruit> getAllFruitsWithParam(@RequestParam Optional<Long> fruitId, @RequestParam Optional<String> fruitName) {

        return fruitService.getAllFruitsWithParam(fruitId, fruitName);
    }

    @GetMapping("/{fruitId}")
    public FruitResponse getOneFruit(@PathVariable Long fruitId) {

        Fruit fruit = fruitService.getOneFruitById(fruitId);
        if (fruit == null) {
            throw new FruitNotFoundException();
        }
        return new FruitResponse(fruit);
    }

    @PostMapping
    Fruit createFruit(@RequestBody FruitCreateRequest newFruitRequest) {
        return fruitService.createNewFruit(newFruitRequest);

    }

    @PutMapping("/{fruitId}")
    public Fruit updateOneFruit(@PathVariable Long fruitId, @RequestBody FruitUpdateRequest fruitUpdateRequest) {
        return fruitService.updateOneFruitById(fruitId, fruitUpdateRequest);
    }

    @DeleteMapping("/{fruitId}")
    public void deleteOneFruit(@PathVariable Long fruitId) {
        fruitService.deleteById(fruitId);

    }

    @ExceptionHandler(FruitNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void handleUserNotFound() {

    }

}
