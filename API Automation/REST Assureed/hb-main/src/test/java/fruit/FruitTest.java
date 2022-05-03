package fruit;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import fruit.models.fruit.Fruit;
import fruit.steps.FruitSteps;
import fruit.utils.TestDataGenerator;

@Epic("Fruit store")
@Feature("Fruit")
@Story("Get")
@DisplayName("Get Fruit")
public class FruitTest extends BaseTest {
    private final FruitSteps fruitSteps = new FruitSteps();
    private final Fruit fullDataFruit = TestDataGenerator.generateFullDataFruit();
    private final int notFoundId = -1;
    private final int foundId = 1;
    private final String defaultName = "Grapes";
    private final Fruit modifiedFruit = fullDataFruit.toBuilder().price(200).build();

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Get fruit by id")
    public void getFruitByIdTest() {
        fruitSteps
                .getFruitWithIdId(foundId);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Get fruit by name")
    public void getFruitByNameTest() {
        fruitSteps
                .getFruitByName(defaultName);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Attempt to request get fruit by non-exist Id ")
    public void getNotFoundFruitTest() {
        fruitSteps
                .getNotFoundFruitById(notFoundId);
    }

    @Test
    @DisplayName("Create fruit successfully")
    public void createFruitTest() {
        fruitSteps
                .createFruitSuccessfully(fullDataFruit)
                .getFruitWithIdId(fullDataFruit.getFruitId());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Update Data for fruit")
    public void updateFullDataFruit() {
        fruitSteps
                .createFruitSuccessfully(fullDataFruit)
                .putFruitSuccessfully(modifiedFruit)
                .assertFruitData(modifiedFruit);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Delete fruit with id")
    public void deleteFullDataFruit() {
        fruitSteps
                .createFruitSuccessfully(fullDataFruit)
                .putFruitSuccessfully(modifiedFruit)
                .deleteFruitById(fullDataFruit.getFruitId());
    }

}