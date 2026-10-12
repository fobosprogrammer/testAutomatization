package ru.t1.authomatization.ui.homework9.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.*;

public class AdminPage {

    /** Заголовок админки. id у него нет — берём по тегу h1. */
    private final SelenideElement adminTitle = $("h1");

    /** Ссылка «Вернуться на сайт». Локатор: по href. */
    private final SelenideElement backToSiteLink = $("a[href='/']");

    private final SelenideElement addProductName = $("#n-name");
    private final SelenideElement addProductPrice = $("#n-price");
    private final SelenideElement addProductButton = $("#add-btn");

    private final SelenideElement toastContainer = $("#toast-container");

    /** Тело таблицы товаров: #tbody (по id). */
    private final SelenideElement tableBody = $("#tbody");

    /** Все строки таблицы товаров: tr внутри tbody. */
    private final ElementsCollection tableRows = $$("#tbody tr");

    public SelenideElement getToastContainer() {
        return toastContainer;
    }

    public SelenideElement getAddProductButton() {
        return addProductButton;
    }

    public SelenideElement getAddProductPrice() {
        return addProductPrice;
    }

    public SelenideElement getAddProductName() {
        return addProductName;
    }

    public SelenideElement getAdminTitle() {
        return adminTitle;
    }

    public SelenideElement getBackToSiteLink() {
        return backToSiteLink;
    }

    @Step("Открыть страницу админки")
    public AdminPage openPage() {
        open("/admin");
        return this;
    }

    @Step("Проверить что страница админки открыта")
    public AdminPage checkPageDisplayed() {
        AdminPageAssert adminPageAssert = new AdminPageAssert(this);
        adminPageAssert.checkPageVisible();
        return this;
    }

    @Step("Добавить товар, имя - {name}, цена продукта - {price}")
    public AdminPage addProduct(String name, double price) {
        addProductName.setValue(name);
        addProductPrice.setValue(String.valueOf(price));
        addProductButton.click();

        AdminPageAssert adminPageAssert = new AdminPageAssert(this);
        adminPageAssert.checkAddMessageIsVisible();
        return this;
    }

    @Step("Отредактировать товар, id продукта - {productId}, новое имя - {newName}, цена продукта - {newPrice}")
    public AdminPage editProduct(long productId, String newName, int newPrice) {
        $("#nm-" + productId).setValue(newName);
        $("#pr-" + productId).setValue(String.valueOf(newPrice));
        $("button[data-action='update'][data-id='" + productId + "']").click();
        AdminPageAssert adminPageAssert = new AdminPageAssert(this);
        adminPageAssert.checkEditMessageIsVisible(productId);
        return this;
    }

    @Step("Получить имя продукта по номеру строки - {rowNumber}")
    public String getProductNameFromRow(int rowNumber) {
        SelenideElement nameInput = tableRows.get(rowNumber).$("input[id^='nm-']");
        if (nameInput.exists()) {
            String name = nameInput.getAttribute("value");
            return name;
        }
        throw new AssertionError("Товар из строки '" + rowNumber + "' не найден в таблице админки");
    }

    @Step("Получить id продукта по номеру строки - {rowNumber}")
    public long getProductIdFromRow(int rowNumber) {
        SelenideElement nameInput = tableRows.get(rowNumber).$("input[id^='nm-']");
        if (nameInput.exists()) {
            // Достаём id из атрибута id элемента: "nm-42" -> "42".
            String id = nameInput.getAttribute("id").substring(3);
            return Long.parseLong(id);
        }
        throw new AssertionError("Товар из строки '" + rowNumber + "' не найден в таблице админки");
    }

    @Step("Найти id продукта по имени - {name}")
    public long findProductIdByName(String name) {
        for (SelenideElement row : tableRows) {
            SelenideElement nameInput = row.$("input[id^='nm-']");
            if (nameInput.exists() && name.equals(nameInput.val())) {
                // Достаём id из атрибута id элемента: "nm-42" -> "42".
                String id = nameInput.getAttribute("id").substring(3);
                return Long.parseLong(id);
            }
        }
        throw new AssertionError("Товар с названием '" + name + "' не найден в таблице админки");
    }
}
