package fruit.models.fruit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor

public class Fruit {
    private long fruitId;
    private String fruitName;
    private long price;
    private long stock;


}
