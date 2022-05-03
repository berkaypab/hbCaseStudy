package com.example.demo.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fruit")
@Data

public class Fruit {
    @Id
    private long fruitId;
    private String fruitName;
    private long price;
    private long stock;
}
