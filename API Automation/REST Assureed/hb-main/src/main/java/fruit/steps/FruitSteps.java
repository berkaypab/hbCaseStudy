package fruit.steps;

import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import fruit.controllers.fruit.FruitController;
import fruit.models.fruit.Fruit;

import java.io.File;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FruitSteps extends FruitController {

    @Step("Check status code {ExpectedStatus}")
    private void assertStatusCode(Response response) {
        assertThat(response.statusCode(), equalTo(HttpStatus.SC_OK));
    }

    @Step("Successfully get fruit by Id {fruitId}")
    public Fruit[] getFruitWithIdId(long fruitId) {
        Response response = getFruit(fruitId);
        JsonPath jsonPath = new JsonPath(response.asString());
        jsonPath.prettyPrint();
        assertStatusCode(response);
        return jsonPath.getObject("$", Fruit[].class);
    }


    @Step("Successfully get fruit by Id {fruitId}")
    public Fruit getFruitById(long fruitId) {
        Response response = getFruit(fruitId);
        assertStatusCode(HttpStatus.SC_OK, response);
        return response.as(Fruit.class);
    }

    @Step("Successfully get fruit by name {fruitName}")
    public Fruit[] getFruitByName(String fruitName) {
        Response response = getFruit(fruitName);
        assertStatusCode(HttpStatus.SC_OK, response);
        JsonPath jsonPath = new JsonPath(response.asString());
        jsonPath.prettyPrint();
        return jsonPath.getObject("$", Fruit[].class);
    }

    @Step("Request non-exist fruit id with get method {fruitId}")
    public void getNotFoundFruitById(long fruitId) {
        Response response = getFruit(fruitId);
        assertStatusCode(HttpStatus.SC_NOT_FOUND, response);
    }

    @Step("Successfully delete an existing fruit by id {fruitId}")
    public FruitSteps deleteFruitById(long fruitId) {
        Response response = deleteFruit(fruitId);
        assertStatusCode(HttpStatus.SC_OK, response);
        return this;
    }

    @Step("Create fruit successfully {fruit}")
    public FruitSteps createFruitSuccessfully(Fruit fruit) {
        Response response = postFruit(fruit);
        assertStatusCode(HttpStatus.SC_OK, response);
        return this;
    }

    @Step("Successful update of fruit data {fruit}")
    public FruitSteps putFruitSuccessfully(Fruit fruit) {
        Response response = putFruit(fruit);
        assertStatusCode(HttpStatus.SC_OK, response);
        return this;
    }

    @Step("Validate fruit fields {expectedFruit}")
    public FruitSteps assertFruitData(Fruit expectedFruit) {
        Fruit[] fruit = getFruitWithIdId(expectedFruit.getFruitId());
        assertThat(fruit[0], equalTo(expectedFruit));
        return this;
    }

    @Step("Check status code {expectedStatus}")
    private void assertStatusCode(int expectedStatus, Response response) {
        assertThat(response.statusCode(), equalTo(expectedStatus));
    }

}
