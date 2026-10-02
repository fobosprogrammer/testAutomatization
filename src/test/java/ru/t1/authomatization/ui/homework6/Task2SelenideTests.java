package ru.t1.authomatization.ui.homework6;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Task2SelenideTests {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String username = System.getProperty("api.username");
    private static final String password = System.getProperty("api.password");
    private static String productName = "Selenide homework6.2";
    @BeforeEach
    public void initBrowser() {
        // Инициализация браузера (Selenide делает это автоматически при первом вызове open/$(...))
        open(BASE_URL);
    }

    @AfterEach
    public void closeBrowser() {
        // Закрытие браузера
        deleteProduct(productName);
        closeWebDriver();
    }

    private void addProduct(String productName, String price) {
        // Логин в админку
        open(BASE_URL + "/login");
        $("#username").val(username);
        $("#password").val(password);
        $("button.primary").click();

        // Добавление товара
        open(BASE_URL + "/admin");
        $("#n-name").val(productName);
        $("#n-price").val(price);
        $("#add-btn").click();
    }

    private void deleteProduct(String productName) {
        open(BASE_URL + "/login");
        $("#username").val(username);
        $("#password").val(password);
        $("button.primary").click();

        // Удаление товара
        open(BASE_URL + "/admin");
        // Находим строку товара
        var row = $("[value='" + productName + "']")
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
    @Test
    @DisplayName("2.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается.")
    public void test2_1_AddProductAndCheckOnVitrine() {
        // 2.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается.
        addProduct(productName,"100");
        // Переход на витрину
        open(BASE_URL + "/");

        // Проверка отображения товара методом shouldBe
        $("[class*='product-card'][data-name='" + productName + "']").find("h4").shouldBe(Condition.exactText(productName));
    }

    @Test
    @DisplayName("2.2. Добавить товар в корзину и проверить, что он отображается.")
    public void test2_2_AddToCartAndCheck() {
        // 2.2. Добавить товар в корзину и проверить, что он отображается.
        addProduct(productName,"100");
        open(BASE_URL + "/");

        // Добавление в корзину
        $("button[data-name='" + productName + "']").click();

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
        addProduct(productName,"100");
        open(BASE_URL + "/");

        // Добавление в корзину
        $("button[data-name='" + productName + "']").click();
        $("button[data-name='" + productName + "']").click();
        $("button[data-name='" + productName + "']").click();
        $("button[data-name='" + productName + "']").click();

        // Нажатие на кнопку корзины а потом оформления заказа
        $("#open-cart-btn").click();
        $("#makeOrder").click();

        // Проверка JS Alert
        Alert alert = Selenide.switchTo().alert();
        assertTrue(alert.getText().equalsIgnoreCase("[SmartShop]: Денег не хватает! Сумма 400 ₽ превышает лимит 300 ₽."),"Алерт отсутствует");
        alert.accept();
    }
}