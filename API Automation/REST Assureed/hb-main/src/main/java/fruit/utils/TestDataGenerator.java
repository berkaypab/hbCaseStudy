package fruit.utils;

import org.apache.commons.lang3.RandomStringUtils;
import fruit.models.fruit.Fruit;

import java.util.Random;

public class TestDataGenerator {

    public static Fruit generateFullDataFruit() {
        return Fruit.builder()
                .fruitId(getRandomInt())
                .fruitName(getRandomString())
                .stock(getRandomInt())
                .price(getRandomInt())
                .build();
    }

    private static Integer getRandomInt() {
        return new Random().nextInt((65536) - 64000);
    }


    private static String getRandomString() {
        return RandomStringUtils.randomAlphabetic(3);
    }
}
