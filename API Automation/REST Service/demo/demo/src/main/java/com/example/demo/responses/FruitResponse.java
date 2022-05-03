package com.example.demo.responses;

import com.example.demo.entities.Fruit;
import lombok.Data;

@Data
public class FruitResponse {
     long fruitId;
     String fruitName;
     long price;
     long stock;

    public FruitResponse(Fruit entity) {

        this.fruitId = entity.getFruitId();
        this.fruitName = entity.getFruitName();
        this.stock = entity.getStock();
        this.price = entity.getPrice();
    }
}
