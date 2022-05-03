package com.example.demo.repos;

import com.example.demo.entities.Fruit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FruitRepository extends JpaRepository<Fruit, Long> {
    List<Fruit> findByFruitId(Long fruitId);

    List<Fruit> findByFruitName(String fruitName);
}