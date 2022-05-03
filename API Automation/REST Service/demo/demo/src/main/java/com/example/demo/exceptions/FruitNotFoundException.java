package com.example.demo.exceptions;

public class FruitNotFoundException extends RuntimeException {

    public FruitNotFoundException() {
        super();
    }

    FruitNotFoundException(String message) {
        super(message);
    }
}
