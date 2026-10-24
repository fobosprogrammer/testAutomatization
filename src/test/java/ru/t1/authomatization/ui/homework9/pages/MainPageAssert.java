package ru.t1.authomatization.ui.homework9.pages;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import org.assertj.core.api.AbstractAssert;

import static com.codeborne.selenide.Condition.visible;

public class MainPageAssert extends AbstractAssert<MainPageAssert,MainPage> {
    protected MainPageAssert(MainPage mainPage, Class<?> selfType) {
        super(mainPage, selfType);
    }

    public MainPageAssert(MainPage actual) {
        super(actual, MainPageAssert.class);
    }

    public MainPageAssert cartButtonIsVisible() {
        return this;
    }

    @Step("Проверить видимость главной страницы")
    public void checkPageDisplayed() {
        actual.getProductsList().should(visible);
    }

    @Step("Проверить видимость модального окна")
    public void cartModalIsVisible() {
        actual.getCartModalWindow().shouldBe(visible);
    }

    @Step("Проверить видимость элемента закрывающего модальное окно")
    public void closeModalIsVisible() {
        actual.getCloseModal().shouldBe(visible);
    }

    @Step("Проверить видимость кнопки сделать заказ")
    public void makeOrderButtonIsVisible() {
        actual.getMakeOrderButton().shouldBe(visible);
    }

    @Step("Првоерить видимость блока уведомлений")
    public void toastContainerIsVsibileAndText(String toastText) {
        actual.getToastContainer().shouldBe(visible);
        actual.getToastContainer().should(Condition.text(toastText));
    }

    @Step("Проверить количество товаров в корзине")
    public void checkExpectedQuantity(int expectedQuantity) {
        actual.getCartCount().shouldBe(Condition.text(String.valueOf(expectedQuantity)));
    }

    @Step("Проверить итоговую сумму в корзине")
    public void checkTotalPrice(double totalPrice) {
        String expectedPrice;
        if (totalPrice == Math.floor(totalPrice)) {
            expectedPrice = String.valueOf((long) totalPrice);
        } else {
            java.text.DecimalFormat df = new java.text.DecimalFormat("#.#");
            expectedPrice = df.format(totalPrice);
        }
        actual.getTotalPrice().shouldBe(Condition.text(expectedPrice));
    }
}
