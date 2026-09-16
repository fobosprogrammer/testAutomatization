package ru.t1.authomatization.api.homework4;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
//import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
public class Task1Tests extends BaseTest {

    // 1. Метод с использованием инструкций given(), when(), then()
    @Test
    @DisplayName("Тест 1: GET /goods/list через BDD стиль")
    void testGetListBddStyle() {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                    .get("/goods/list")
                .then()
                    .statusCode(200) // Проверка кода ответа
                    .body("goods", equalTo(Collections.emptyList())); // Проверка, что список товаров пуст
    }

    // 2. Метод с использованием RequestSpecification
    @Test
    @DisplayName("Тест 2: GET /goods/list через RequestSpecification")
    void testGetListRequestSpec() {
        RequestSpecification requestSpec =  new RequestSpecBuilder()
                .setBaseUri("http://localhost:8080")
                .setContentType(ContentType.JSON)
                .build();

        given()
                .spec(requestSpec) // Применяем спецификацию запроса
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200) // Проверка кода ответа
                .body("goods", equalTo(Collections.emptyList())); // Проверка, что тело ответа пустое или null
    }

    // 3. Создание товара и проверка через встроенные проверки REST Assured
    @Test
    @DisplayName("Тест 3: POST /goods/add и проверка через REST Assured matchers")
    void testCreateAndVerifyWithRestAssured() {
        // Шаг 1: Создаем товар
        String productName = "TestProduct_" + System.currentTimeMillis();
        double price = 100.50;
        String requestBody = "{\n" +
                "  \"name\": \"" + productName + "\",\n" +
                "  \"price\": \"" + price + "\"\n" +
                "}";

        Response response = given()
                .log().all()
                .auth().basic(getUsername(), getPassword())
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/goods/add")
                .then()
                .statusCode(200)
                .extract().response();
        // Извлекаем id из ответа
        long productId = response.jsonPath().getLong("data.id");

        // Шаг 2: Получаем список и проверяем наличие товара
        given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                // Проверяем, что в списке товаров есть продукт с таким именем
                .body("goods.name", org.hamcrest.Matchers.hasItem(productName));

        given()
                .auth().basic(getUsername(), getPassword())
                .pathParam("id", productId)
                .when()
                .delete("/goods/{id}")
                .then()
                .statusCode(200);

    }

    // 4. Создание товара и проверка через AssertJ
    @Test
    @DisplayName("Тест 4: POST /goods/add и проверка через AssertJ")
    void testCreateAndVerifyWithAssertJ() {
        // Шаг 1: Создаем товар
        String productName = "TestProduct_AssertJ_" + System.currentTimeMillis();
        double price = 200.00;
        String requestBody = "{\n" +
                "  \"name\": \"" + productName + "\",\n" +
                "  \"price\": \"" + price + "\"\n" +
                "}";

        Response responseAdd = given()
                .log().all()
                .auth().basic(getUsername(), getPassword())
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/goods/add")
                .then()
                .statusCode(200)
                .extract().response();

        // Извлекаем id из ответа
        long productId = responseAdd.jsonPath().getLong("data.id");
        // Шаг 2: Получаем список
        Response responseGet = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .extract().response();

        // Шаг 3: Парсим и проверяем через AssertJ
        List<Map<String, Object>> goodsList = responseGet.jsonPath().getList("goods");

        assertThat(responseGet.getStatusCode()).isEqualTo(200);

        boolean found = goodsList.stream()
                .anyMatch(p -> productName.equals(p.get("name")));

        assertThat(found).isTrue()
                .as("Товар %s должен быть в списке", productName);

        given()
                .auth().basic(getUsername(), getPassword())
                .pathParam("id", productId)
                .when()
                .delete("/goods/{id}")
                .then()
                .statusCode(200);
    }
}
