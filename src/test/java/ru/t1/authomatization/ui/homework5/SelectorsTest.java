package ru.t1.authomatization.ui.homework5;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.aeonbits.owner.ConfigFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import ru.t1.authomatization.config.AppConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("ui")
public class SelectorsTest extends BaseUiTest{

    private static String productForTestOne = "Стакан";
    private static String productForTestTwo = "Test";
    protected static AppConfig config;

    static {
        config = ConfigFactory.create(AppConfig.class);
    }

    @BeforeAll
    static void setUpAndClean() {
        cleanupCreatedProducts();
        addProduct(productForTestOne,25);
        addProduct(productForTestTwo,123);
    }

    @AfterAll
    static void closeTests() {
        cleanupCreatedProducts();
    }

    @Step("API: Удалить созданные тестами продукты")
    static void cleanupCreatedProducts() {

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

        List<String> idsToDelete = goodsList.stream()
                .filter(p -> productForTestOne.equals(p.get("name")) || productForTestTwo.equals(p.get("name")))
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
    private static long addProduct(String productName, double price) {
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
    @DisplayName("1.1. Список названий товаров")
    public void getNames() {

        ElementsCollection productTitles = $$("#products-list .product-card h4");
        assertTrue(productTitles.size() > 0,"Товаров нет в списке");
        // Собираем все названия в список
        List<String> namesList2 = productTitles.stream()
                .map(SelenideElement::getText)
                .collect(Collectors.toList());

        // Проверяем наличие конкретного товара
        assertTrue(namesList2.contains("Test"), "Товар 'Test' не найден");
    }

    @Test
    @DisplayName("1.2. Список цен товаров")
    public void getPrices() {

        ElementsCollection productPrices = $$("#products-list .product-card h4 + div");
        assertTrue(productPrices.size() > 0,"Товаров нет в списке");
        // Собираем все названия в список
        List<String> namesList2 = productPrices.stream()
                .map(SelenideElement::getText)
                .collect(Collectors.toList());

        // Проверяем наличие конкретного товара
        assertTrue(namesList2.contains("123 ₽"), "Товар 'Test' не найден");
    }

    @Test
    @DisplayName("1.3. Название товара с ценой 25")
    public void getPrice25() {

        SelenideElement productName =
                $x("//div[contains(@class,'product-card')]" +
                        "[.//div[contains(normalize-space(),'25 ₽')]]" +
                        "//h4");

        assertFalse(productName.getText().isBlank(), "Название товара не должно быть пустым");
    }


    @Test
    @DisplayName("1.4. Цена товара с названием «Стакан»")
    void shouldFindPriceOfGlass() {

        SelenideElement price =
                $x("//div[contains(@class,'product-card')]" +
                        "[.//h4[normalize-space()='Стакан']]" +
                        "//div[contains(normalize-space(),'₽')]");

        price
                .shouldBe(visible)
                .shouldHave(text("₽"));

        assertFalse(price.getText().isBlank(), "Цена товара «Стакан» не должна быть пустой");
    }

    @Test
    @DisplayName("1.5. Список карточек товаров в корзине")
    void shouldFindCartItems() {
        $("[data-action='add-to-cart']").click();
        $("#open-cart-btn").click();

        ElementsCollection cartItems =
                $$("#cart-items .cart-item");

        assertFalse(cartItems.isEmpty(), "В корзине нет товаров");

        cartItems.shouldHave(sizeGreaterThan(0));

        cartItems.forEach(item ->
                item.shouldBe(visible)
                        .shouldNot(Condition.text(")"))
        );
    }

    @Test
    @DisplayName("1.6. Кнопка корзины")
    void shouldFindCartButton() {

        SelenideElement cartButton =
                $("#open-cart-btn");

        cartButton
                .shouldBe(visible)
                .shouldBe(enabled);

        assertTrue(cartButton.isDisplayed(), "Кнопка корзины должна отображаться");
        assertTrue(cartButton.isEnabled(), "Кнопка корзины должна быть доступна");
    }

}
