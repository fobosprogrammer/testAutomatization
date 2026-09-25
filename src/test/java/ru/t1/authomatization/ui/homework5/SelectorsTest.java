package ru.t1.authomatization.ui.homework5;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("ui")
public class SelectorsTest extends BaseUiTest{

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

        $("#open-cart-btn").click();

        ElementsCollection cartItems =
                $$("#cart-items .cart-item");

        assertFalse(cartItems.isEmpty(), "В корзине нет товаров");

        cartItems.shouldHave(sizeGreaterThan(0));

        cartItems.forEach(item ->
                item.shouldBe(visible)
                        .shouldNotHave(text(""))
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
