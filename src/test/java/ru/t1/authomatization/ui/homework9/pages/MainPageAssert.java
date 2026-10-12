package ru.t1.authomatization.ui.homework9.pages;

import com.codeborne.selenide.Condition;
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

    public void checkPageDisplayed() {
        actual.getProductsList().should(visible);
    }

    public void cartModalIsVisible() {
        actual.getCartModalWindow().shouldBe(visible);
    }

    public void closeModalIsVisible() {
        actual.getCloseModal().shouldBe(visible);
    }

    public void makeOrderButtonIsVisible() {
        actual.getMakeOrderButton().shouldBe(visible);
    }

    public void toastContainerIsVsibileAndText(String toastText) {
        actual.getToastContainer().shouldBe(visible);
        actual.getToastContainer().should(Condition.text(toastText));
    }

    public void checkExpectedQuantity(int expectedQuantity) {
        actual.getCartCount().shouldBe(Condition.text(String.valueOf(expectedQuantity)));
    }

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
