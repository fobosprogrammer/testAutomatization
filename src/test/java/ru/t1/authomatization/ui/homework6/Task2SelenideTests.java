package ru.t1.authomatization.ui.homework6;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.aeonbits.owner.ConfigFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import ru.t1.authomatization.config.AppConfig;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Task2SelenideTests {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String username = System.getProperty("api.username");
    private static final String password = System.getProperty("api.password");
    private static String productNameOne = "Selenide homework6.2";
    private static String productNameTwo = "Selenide homework6.2";
    protected static AppConfig config;

    static {
        config = ConfigFactory.create(AppConfig.class);
    }

    @BeforeEach
    public void initBrowser() {
        // Инициализация браузера (Selenide делает это автоматически при первом вызове open/$(...))
        open(BASE_URL);
    }

    @AfterEach
    public void closeBrowser() {
        // Закрытие браузера
        cleanupCreatedProducts();
        closeWebDriver();
    }

    @AfterEach
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
                .filter(p -> productNameOne.equals(p.get("name")) || productNameTwo.equals(p.get("name")))
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
    @DisplayName("2.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается.")
    public void test2_1_AddProductAndCheckOnVitrine() {
        // 2.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается.
        addProduct(productNameOne,100);
        // Переход на витрину
        open(BASE_URL + "/");

        // Проверка отображения товара методом shouldBe
        $("[class*='product-card'][data-name='" + productNameOne + "']").find("h4").shouldBe(Condition.exactText(productNameOne));
    }

    @Test
    @DisplayName("2.2. Добавить товар в корзину и проверить, что он отображается.")
    public void test2_2_AddToCartAndCheck() {
        // 2.2. Добавить товар в корзину и проверить, что он отображается.
        addProduct(productNameOne,100);
        open(BASE_URL + "/");

        // Добавление в корзину
        $("button[data-name='" + productNameOne + "']").click();

        // Проверка счетчика корзины
        $("#cart-count").should(Condition.visible);
        // Более строгая проверка: текст должен быть больше 0
        String count = $("#cart-count").getText();
        assertTrue(Integer.parseInt(count) > 0, "Корзина пуста");
    }

    @Test
    @DisplayName("2.3. Попытаться войти в админку с неверным логином и паролем.")
    public void test2_3_FailedLogin() {
        // 2.3. Попытаться войти в админку с неверным логином и паролем.

        open(BASE_URL + "/login");


        $("#username").val("wrong_user");
        $("#password").val("wrong_pass");
        $("button.primary").click();

        // Проверка ошибки
        $(".alert-danger")
                .shouldBe(Condition.visible) // Сначала ждем появления и видимости
                .shouldHave(Condition.exactText("Неверные учетные данные пользователя")); // Затем проверяем текст
    }

    @Test
    @DisplayName("2.4. Проверить сохранение товаров в корзине после обновления страницы.")
    public void test2_4_CartPersistence() {
        // 2.4. Проверить сохранение товаров в корзине после обновления страницы.
        addProduct(productNameOne,30);
        open(BASE_URL + "/");

        // Добавляем товар
        $("[data-action='add-to-cart']").click();

        // Обновляем страницу
        Selenide.refresh();

        // Проверяем наличие в корзине
        $("#cart-count").should(Condition.visible);
        String count = $("#cart-count").getText();
        assertTrue(Integer.parseInt(count) == 0, "Товар присутствует в корзине");
    }

    @Test
    @DisplayName("2.5. Добавить в корзину товаров более чем на 300 рублей и нажать на кнопку «Оформить заказ».")
    public void test2_5_CheckoutAlert() {
        // 2.5. Добавить в корзину товаров более чем на 300 рублей и нажать на кнопку «Оформить заказ».
        // Проверить, что отображается JS Alert.
        addProduct(productNameOne,100);
        open(BASE_URL + "/");

        // Добавление в корзину
        $("button[data-name='" + productNameOne + "']").click();
        $("button[data-name='" + productNameOne + "']").click();
        $("button[data-name='" + productNameOne + "']").click();
        $("button[data-name='" + productNameOne + "']").click();

        // Нажатие на кнопку корзины а потом оформления заказа
        $("#open-cart-btn").click();
        $("#makeOrder").click();

        // Проверка JS Alert
        Alert alert = Selenide.switchTo().alert();
        assertTrue(alert.getText().equalsIgnoreCase("[SmartShop]: Денег не хватает! Сумма 400 ₽ превышает лимит 300 ₽."),"Алерт отсутствует");
        alert.accept();
    }
}