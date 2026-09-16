package ru.t1.authomatization.api.homework4;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
public class Task2Tests extends BaseTest  {

    // Созданные в тестах id товаров — удаляем их после каждого теста,
    // чтобы не засорять сервис и не ловить его баг с большим списком.
    private final List<Long> createdIds = new ArrayList<>();
    /**
     * Удаляет все товары, созданные в ходе теста.
     * Вызывается JUnit после каждого теста (в т.ч. после падения),
     * поэтому тесты независимы друг от друга и перезапускаемы.
     */
    @AfterEach
    void cleanupCreatedProducts() {
        for (Long id : createdIds) {
            deleteGoodsRecord(id);
        }
        createdIds.clear();
    }

    private void deleteGoodsRecord(Long goodsId) {
        given()
                .auth().basic(getUsername(), getPassword())
                .pathParam("id", goodsId)
                .when()
                .delete("/goods/{id}")
                .then()
                .statusCode(200);

    }

    // GET /goods/list
    @Test
    void testGetList_Success() {
        Response response = given().when().get("/goods/list");
        assertThat(response.statusCode()).isEqualTo(200);
        List<Map<String, Object>> data = response.jsonPath().getList("goods");
        assertThat(data).isNotNull(); // Проверка: не null
    }

    // POST /goods/add
    @Test
    void testAddProduct_Success() {
        String name = "Test Product " + System.currentTimeMillis();
        Response response = given()
                .contentType(ContentType.JSON)
                .auth().basic(getUsername(), getPassword())
                .body(Map.of("name", name, "price", 99.99))
                .when()
                .post("/goods/add");

        assertThat(response.statusCode()).isEqualTo(200);
        long productId = response.jsonPath().getLong("data.id");
        assertThat(productId).isGreaterThan(0);
        createdIds.add(productId);
    }

    @Test
    void testAddProduct_MissingName() {
        Response response = given()
                .auth().basic(getUsername(), getPassword())
                .contentType(ContentType.JSON)
                .body(Map.of("price", 99.99))
                .when()
                .post("/goods/add");

        assertThat(response.statusCode()).isEqualTo(400);
    }

    @Test
    void testAddProduct_NegativePrice() {
        Response response = given()
                .auth().basic(getUsername(), getPassword())
                .contentType(ContentType.JSON)
                .body(Map.of("name", "Test", "price", -10.0))
                .when()
                .post("/goods/add");

        assertThat(response.statusCode()).isEqualTo(400);
    }

    // GET /goods/{id}
    @Test
    void testGetProductById_Success() {
        // Сначала добавим товар
        String name = "GetById Product";
        Response addResponse = given()
                .auth().basic(getUsername(), getPassword())
                .contentType(ContentType.JSON)
                .body(Map.of("name", name))
                .when()
                .post("/goods/add");

        long productId = addResponse.jsonPath().getLong("data.id");

        // Теперь получаем его
        Response getResponse = given()
                .auth().basic(getUsername(), getPassword())
                .pathParam("id", productId)
                .when()
                .get("/goods/{id}");

        assertThat(getResponse.statusCode()).isEqualTo(200);
        assertThat(getResponse.jsonPath().getString("name")).isEqualTo(name);

        createdIds.add(productId);
    }

    @Test
    void testGetProductById_NotFound() {
        given()
                .auth().basic(getUsername(), getPassword())
                .pathParam("id", 999999)
                .when()
                .get("/goods/{id}")
                .then()
                .statusCode(500);
    }

    // DELETE /goods/{id}
    @Test
    void testDeleteProduct_Success() {
        // Добавляем товар
        String name = "ToDelete Product";
        Response addResponse = given()
                .auth().basic(getUsername(), getPassword())
                .contentType(ContentType.JSON)
                .body(Map.of("name", name))
                .when()
                .post("/goods/add");

        long productId = addResponse.jsonPath().getLong("data.id");

        // Удаляем
        Response deleteResponse = given()
                .auth().basic(getUsername(), getPassword())
                .pathParam("id", productId)
                .when()
                .delete("/goods/{id}");

        assertThat(deleteResponse.statusCode()).isEqualTo(200);

        // Проверяем, что удален
        given()
                .auth().basic(getUsername(), getPassword())
                .pathParam("id", productId)
                .when()
                .get("/goods/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    void testDeleteProduct_NotFound() {
        given()
                .auth().basic(getUsername(), getPassword())
                .pathParam("id", 999999)
                        .when()
                        .delete("/goods/{id}")
                        .then()
                        .statusCode(404);
    }

    // PATCH /goods/{id}
    @Test
    void testPatchProduct_Success() {
        // Добавляем товар
        String oldName = "Old Name";
        Response addResponse = given()
                .auth().basic(getUsername(), getPassword())
                .contentType(ContentType.JSON)
                .body(Map.of("name", oldName))
                .when()
                .post("/goods/add");

        long productId = addResponse.jsonPath().getLong("data.id");

        // Обновляем
        String newName = "Updated Name";
        Response patchResponse = given()
                .auth().basic(getUsername(), getPassword())
                .pathParam("id", productId)
                .contentType(ContentType.JSON)
                .body(Map.of("name", newName))
                .when()
                .patch("/goods/{id}");

        assertThat(patchResponse.statusCode()).isEqualTo(200);

        // Проверяем обновление
        Response getResponse = given()
                .auth().basic(getUsername(), getPassword())
                .pathParam("id", productId)
                .when()
                .get("/goods/{id}");

        assertThat(getResponse.statusCode()).isEqualTo(200);
        assertThat(getResponse.jsonPath().getString("name")).isEqualTo(newName);

        createdIds.add(productId);
    }

    @Test
    void testPatchProduct_BadName() {
        // Добавляем товар
        String name = "Patch Test";
        Response addResponse = given()
                .auth().basic(getUsername(), getPassword())
                .contentType(ContentType.JSON)
                .body(Map.of("name", name))
                .when()
                .post("/goods/add");

        long productId = addResponse.jsonPath().getLong("data.id");

        // Пытаемся обновить на пустое имя
        Response patchResponse = given()
                .auth().basic(getUsername(), getPassword())
                .pathParam("id", productId)
                .contentType(ContentType.JSON)
                .body(Map.of("name", ""))
                .when()
                .patch("/goods/{id}");

        assertThat(patchResponse.statusCode()).isEqualTo(400);

        createdIds.add(productId);
    }

    @Test
    void testPatchProduct_NotFound() {
        given()
                .auth().basic(getUsername(), getPassword())
                .pathParam("id", 999999)
                .contentType(ContentType.JSON)
                .body(Map.of("name", "New Name"))
                .when()
                .patch("/goods/{id}")
                .then()
                .statusCode(404);
    }
}
