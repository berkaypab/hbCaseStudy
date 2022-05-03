package com.example.demo.services;

import com.example.demo.entities.Fruit;
import com.example.demo.repos.FruitRepository;
import com.example.demo.requests.FruitCreateRequest;
import com.example.demo.requests.FruitUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FruitService {
    FruitRepository fruitRepository;


    FruitService(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;


    }

    public List<Fruit> getAllFruitsWithParam(Optional<Long> fruitId, Optional<String> fruitName) {
        if (fruitId.isPresent()) {
            return fruitRepository.findByFruitId(fruitId.get());
        } else if (fruitName.isPresent()) {
            return fruitRepository.findByFruitName(fruitName.get());
        } else {
            return fruitRepository.findAll();
        }
    }


    public Fruit createNewFruit(FruitCreateRequest newFruitRequest) {
        Fruit fruit = getOneFruitById(newFruitRequest.getFruitId());
        if (fruit != null) {
            return null;
        }
        Fruit fruitToSave = new Fruit();
        fruitToSave.setFruitId(newFruitRequest.getFruitId());
        fruitToSave.setFruitName(newFruitRequest.getFruitName());
        fruitToSave.setStock(newFruitRequest.getStock());
        fruitToSave.setPrice(newFruitRequest.getPrice());

        return fruitRepository.save(fruitToSave);
    }

    public void deleteById(Long fruitId) {
        fruitRepository.deleteById(fruitId);
    }

    public Fruit getOneFruitById(Long fruitId) {
        return fruitRepository.findById(fruitId).orElse(null);
    }

    public Fruit updateOneFruitById(Long fruitId, FruitUpdateRequest request) {
        Optional<Fruit> fruit = fruitRepository.findById(fruitId);
        if (fruit.isPresent()) {
            Fruit fruitToUpdate = fruit.get();
            fruitToUpdate.setPrice(request.getPrice());
            fruitToUpdate.setStock(request.getStock());
            fruitRepository.save(fruitToUpdate);
            return fruitToUpdate;
        } else
            return null;
    }
}
