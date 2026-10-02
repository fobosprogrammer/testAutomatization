package ru.t1.authomatization.ui.homework7;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("ui")
public class Task1Test {

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
    }

    @Test
    @DisplayName("1.1 Перетащить элемент в корзину с помощью Drag-and-Drop.")
    void dragAndDrop() {
        // Получаем первую карточку и проверяем ее атрибуты
        SelenideElement firstProductCard = $$(".product-card").first();
        firstProductCard.shouldBe(visible).shouldHave(Condition.attribute("draggable", "true"));
        SelenideElement cart = $("#open-cart-btn");

        //Делаем Drag-and-Drop
        firstProductCard.dragAndDrop(DragAndDropOptions.to(cart));

        //Проверка наличия товара в корзине
        String productId = firstProductCard.getAttribute("data-id");
        $("#cart-item-" + productId).shouldBe(exist);

    }

    @Test
    @DisplayName("1.2 Удалить добавленный элемент из корзины и проверить, что он там больше не отображается.")
    void deleteElementFromCart() {
        // Получаем первую карточку переносим ее в корзину
        SelenideElement firstProductCard = $$(".product-card").first();
        firstProductCard.shouldBe(visible).shouldHave(Condition.attribute("draggable", "true"));
        SelenideElement cart = $("#open-cart-btn");
        firstProductCard.dragAndDrop(DragAndDropOptions.to(cart));

        //Удаляем карточку из корзины
        $("#open-cart-btn").click();
        String productId = firstProductCard.getAttribute("data-id");
        $("#cart-item-" + productId + " > button").click();

        String count = $("#cart-count").getText();
        assertTrue(Integer.parseInt(count) == 0, "Товар присутствует в корзине");
    }

}
