package ru.t1.authomatization.ui.homework6;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Alert;

import java.util.Map;

import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("ui")
public class Task3Tests {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String username = System.getProperty("api.username");
    private static final String password = System.getProperty("api.password");
    private static String productName = "Selenide homework6.3_1";
    private static String productName2 = "Selenide homework6.3_2";
    @BeforeEach
    public void initBrowser() {
        // Инициализация браузера (Selenide делает это автоматически при первом вызове open/$(...))
        open(BASE_URL);
    }

    @AfterEach
    public void closeBrowser() {
        // Закрытие браузера
        deleteProduct(productName);
        deleteProduct(productName2);
        closeWebDriver();
    }

    private void addProduct(String addProductName, String price) {
        // Логин в админку
        open(BASE_URL + "/login");
        $("#username").val(username);
        $("#password").val(password);
        $("button.primary").click();

        // Добавление товара
        open(BASE_URL + "/admin");
        $("#n-name").val(addProductName);
        $("#n-price").val(price);
        $("#add-btn").click();
    }

    private void deleteProduct(String addProductName) {
        open(BASE_URL + "/login");
        $("#username").val(username);
        $("#password").val(password);
        $("button.primary").click();

        // Удаление товара
        open(BASE_URL + "/admin");
        // Находим строку товара
        var row = $("[value='" + addProductName + "']")
                .parent() // td с input
                .parent(); // tr с данными товара

        // Проверяем, существует ли кнопка удаления в этой строке
        if (row.$("[data-action='delete']").exists()) {
            // Если кнопка есть, кликаем по ней
            row.$("[data-action='delete']").click();

            // Подтверждаем удаление в диалоговом окне
            Selenide.switchTo().alert().accept();
        }
    }

    /**
     * 3.1. Добавить три единицы товара в корзину и оплатить их (общая стоимость <= 300 руб).
     * Проверить уведомление об обработке заказа.
     */
    @Test
    @DisplayName("3.1. Добавить три единицы товара в корзину и оплатить их (общая стоимость <= 300 руб).")
    void testBuyThreeItemsWithinLimit() {
        // Переход на главную страницу
        addProduct(productName,"50");
        open(BASE_URL + "/");

        // Добавление в корзину трех товаров
        $("button[data-name='" + productName + "']").click();
        $("button[data-name='" + productName + "']").click();
        $("button[data-name='" + productName + "']").click();

        // Переход в корзину
        // Нажатие на кнопку корзины а потом оформления заказа
        $("#open-cart-btn").click();
        $("#makeOrder").click();

        $("cartModal").shouldBe(hidden);
    }

    /**
     * 3.2. Добавить в корзину несколько разных товаров и проверить, что общая цена считается корректно.
     */
    @Test
    @DisplayName("3.2. Добавить в корзину несколько разных товаров и проверить, что общая цена считается корректно.")
    void testCartTotalCalculation() {
        addProduct(productName,"100");
        addProduct(productName2,"70");
        open("/");

        // Добавляем первый товар (цена 100)
        $("button[data-name='" + productName + "']").click();

        // Добавляем второй товар (цена 70)
        $("button[data-name='" + productName2 + "']").click();

        // Переход в корзину
        $("#open-cart-btn").click();

        // Ожидаем, что сумма будет 170
        Selenide.$("#total-price").shouldHave(Condition.text("170"));
    }

    /**
     * 3.3. Войти в админку и добавить товар. Проверить уведомление после добавления товара.
     * Требование 3.5: Использование API для генерации тестовых данных (например, получение списка категорий или токена).
     */
    @Test
    @DisplayName("3.3. Войти в админку и добавить товар. Проверить уведомление после добавления товара.")
    void testAdminAddProductWithApiData() {
        // Войти в админку
        open(BASE_URL + "/login");
        $("#username").val(username);
        $("#password").val(password);
        $("button.primary").click();

        // Добавление товара
        open(BASE_URL + "/admin");
        $("#n-name").val(productName);
        $("#n-price").val("100");
        $("#add-btn").click();

        $("#toast-container").should(Condition.visible, Condition.text("Товар успешно добавлен"));
    }

    /**
     * 3.4. Войти в админку и отредактировать товар. Выйти на список товаров и проверить, что изменения применились.
     */
    @Test
    @DisplayName("3.4. Войти в админку и отредактировать товар. Выйти на список товаров и проверить, что изменения применились.")
    void testAdminEditProduct() {
        addProduct(productName,"100");

        // Логин в админку
        open(BASE_URL + "/login");
        $("#username").val(username);
        $("#password").val(password);
        $("button.primary").click();


        open(BASE_URL + "/admin");

        String number = $("[value='"+ productName + "']").closest("tr").$("td").getText();

        $("#nm-" + number).setValue(productName2);
        $("[value='" + productName +"']").closest("tr").$("button.btn-upd").click();

        $("#nm-" + number).shouldBe(Condition.value(productName2));

    }

    @Test
    @DisplayName("3.5")
    void testResAssured() {
        Response loginResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .post("/login?username=admin&password=secret123")
                .then()
                .statusCode(302)
                .extract()
                .response(); // Получаем полный ответ, чтобы извлечь куки

        // Извлекаем куки из ответа логина
        Map<String, String> loginCookies = loginResponse.getCookies();

        String createdProductId = RestAssured.given()
                .contentType(ContentType.JSON)
                .cookies(loginCookies)
                .body("{\"name\": \"" + productName +"\", \"price\": 100}")
                .when()
                .post("/goods/add")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

}
