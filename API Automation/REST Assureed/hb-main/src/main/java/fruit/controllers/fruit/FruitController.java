package fruit.controllers.fruit;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import fruit.models.fruit.Fruit;

import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public class FruitController {

    File schema = new File(System.getProperty("user.dir") + "/schema1.json");
    private final RequestSpecification requestSpecification = new RequestSpecBuilder()
            .log(LogDetail.ALL)
            .setContentType(ContentType.JSON)
            .addFilter(new AllureRestAssured())
            .build();

    public Response getFruit(long fruitId) {

        return given(requestSpecification)
                .queryParams("fruitId", fruitId)
                .when()
                .get().then().body(matchesJsonSchema(schema)).extract().response();
    }


    public Response getFruit(String fruitName) {

        return given(requestSpecification)
               .queryParams("fruitName", fruitName)
               .when()
               .get().then().body(matchesJsonSchema(schema)).extract().response();
    }

    public Response postFruit(Object fruit) {
        return given(requestSpecification)
                .when()
                .body(fruit)
                .post();
    }

    public Response putFruit(Fruit fruit) {
        return given(requestSpecification)
                .when()
                .body(fruit)
                .put(String.valueOf(fruit.getFruitId()));
    }

    public Response deleteFruit(long fruitId) {
        return given(requestSpecification)
                .when()
                .delete(String.valueOf(fruitId));
    }

    public ValidatableResponse JSONSchemaValidation(Response response) {
        return response.then().body(matchesJsonSchema(schema));

    }

}
