package ru.t1.authomatization.ui.homework9;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.aeonbits.owner.ConfigFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.t1.authomatization.config.AppConfig;
import ru.t1.authomatization.ui.homework6.BaseTest;
import ru.t1.authomatization.ui.homework9.pages.AdminPage;
import ru.t1.authomatization.ui.homework9.pages.LoginPage;
import ru.t1.authomatization.ui.homework9.pages.MainPage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CartTests extends BaseTest{

    private String productForTest = "Test Product Homework9";
    private String productForTestTwo = "Test Product Homework9_2";
    protected static AppConfig config;
    private static String loginPage = "/login";
    private static String adminPage = "/admin";

   static {
        config = ConfigFactory.create(AppConfig.class);
    }

    @BeforeEach
    void setUpAndClean() {
        cleanupCreatedProducts();
    }

    @AfterEach
    void clean() {
        cleanupCreatedProducts();
    }

    @Step("Удалить созданные тестами продукты")
    void cleanupCreatedProducts() {

        Response responseGet = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .extract().response();

        // Шаг 3: Парсим и проверяем через AssertJ
        List<Map<String, Object>> goodsList = responseGet.jsonPath().getList("goods");

        Assertions.assertThat(responseGet.getStatusCode()).isEqualTo(200);

        List<String> idsToDelete = goodsList.stream()
                .filter(p -> productForTest.equals(p.get("name")) || productForTestTwo.equals(p.get("name")))
                .map(p -> p.get("id").toString())
                .collect(Collectors.toList());

        for (String id : idsToDelete) {
            given()
                    .auth().basic(config.username(), config.password())
                    .pathParam("id", id)
                    .when()
                    .delete("/goods/{id}")
                    .then()
                    .statusCode(200);
        }
    }

    @Step("API: создать товар «{productName}» (цена {price})")
    private long addProduct(String productName,double price) {
        String name = "Test Product " + System.currentTimeMillis();
        Response response = given()
                .contentType(ContentType.JSON)
                .auth().basic(config.username(), config.password())
                .body(Map.of("name", productName, "price", price))
                .when()
                .post("/goods/add");

        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        return response.jsonPath().getLong("data.id");
    }

    @Test
    @DisplayName("2.1. Добавить три единицы товара в корзину и оплатить их (общая стоимость не должна превышать 300 рублей). Проверить уведомление об обработке заказа.")
    public void testAddThreeCheapestItemsAndPay() {
        long productId = addProduct(productForTest,50);
        MainPage mainPage = new MainPage().openPage();
        mainPage.checkPageDisplayed();

        mainPage.checkCartCountNumber(0);
        mainPage.addToCart(3,productId);
        mainPage.checkCartCountNumber(3);

        mainPage.clickCartButton();
        mainPage.clickMakeOrder();
        mainPage.isMakeOrderVisibility();
    }

    @Test
    @DisplayName("2.2. Добавить в корзину несколько разных товаров и проверить, что общая цена в корзине считается корректно.")
    public void testAddTwoItems() {
        double priceOne = 50;
        double priceTwo = 60;
        long productIdOne = addProduct(productForTest,priceOne);
        long productIdtwo = addProduct(productForTestTwo,priceTwo);
        MainPage mainPage = new MainPage().openPage();
        mainPage.checkPageDisplayed();
        mainPage.checkCartCountNumber(0);

        mainPage.addToCart(1,productIdOne);
        mainPage.addToCart(1,productIdtwo);
        mainPage.checkCartCountNumber(2);
        mainPage.checkCartPrice(priceOne + priceTwo);
    }

    @Test
    @DisplayName("2.3. Войти в админку и добавить товар. Проверить уведомление после добавления товара.")
    public void addItemInAdminAndCheck() {
        LoginPage loginPage = new LoginPage().openPage();
        loginPage.checkPageDisplayed();

        AdminPage adminPage = loginPage.loginAs(config.username(), config.password());
        adminPage.checkPageDisplayed();

        adminPage.addProduct(productForTest,79);
    }

    @Test
    @DisplayName("2.4. Войти в админку и отредактировать товар. Выйти на список товаров и проверить, что изменения применились.")
    public void editItemInAdminAndCheck() {
        long productIdOne = addProduct(productForTest,50);
        LoginPage loginPage = new LoginPage().openPage();
        loginPage.checkPageDisplayed();

        AdminPage adminPage = loginPage.loginAs(config.username(), config.password());
        adminPage.checkPageDisplayed();
        long productId = adminPage.getProductIdFromRow(0);
        adminPage.editProduct(productId, productForTestTwo,100);
    }

}
