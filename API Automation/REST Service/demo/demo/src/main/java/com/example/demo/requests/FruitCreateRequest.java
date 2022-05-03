package com.example.demo.requests;

import lombok.Data;

@Data
public class FruitCreateRequest {
    private long fruitId;
    private String fruitName;
    private long price;
    private long stock;
}
