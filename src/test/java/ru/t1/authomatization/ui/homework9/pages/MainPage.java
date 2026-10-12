package ru.t1.authomatization.ui.homework9.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {

    /** Контейнер списка товаров: #products-list (локатор по id). */
    private final SelenideElement productsList = $("#products-list");

    /** Все карточки товаров на странице: .product-card (id у них нет). */
    private final ElementsCollection productCards = $$(".product-card");

    private final SelenideElement mainTitle = $("#main-title");

    /** Ссылка «Администрирование» — ведёт в админку. Локатор: по href. */
    private final SelenideElement adminLink = $("a[href='/admin']");

    /** Кнопка открытия корзины. Локатор: по id (#open-cart-btn). */
    private final SelenideElement cartButton = $("#open-cart-btn");

    private final SelenideElement cartCount = $("#cart-count");
    private final SelenideElement cartModalWindow = $("#cartModal");
    private final SelenideElement closeModal = $("#close-modal");
    private final SelenideElement makeOrderButton = $("#makeOrder");
    private final SelenideElement toastContainer = $("#toast-container");
    private final SelenideElement totalPrice = $("#total-price");

    // =================================================================

    /**
     * Открывает главную страницу по относительному адресу "/"
     * (baseUrl подставится из UiConfig).
     *
     * @return this — PageObject возвращает себя для цепочек
     */
    public MainPage openPage() {
        open("/");
        return this;
    }

    public void cartCountClick() {
        cartCount.click();
    }
    public void closeModalClick() {
        closeModal.click();
    }
    public void makeOrderButtonClick() {
        makeOrderButton.click();
    }

    public void clickToCartById(long productId) {
    }

    public MainPage checkPageDisplayed() {
        MainPageAssert mainPageAssert = new MainPageAssert(this);
        mainPageAssert.checkPageDisplayed();
        return this;

    }

    public MainPage clickToProductByName(String productName) {
        $("button[data-name='" + productName + "']").click();
        return this;
    }

    public void addToCart(int quantity, long productId) {
        SelenideElement card = $("#card-" + productId);
        SelenideElement qtyInput = card.find("#q-" + productId);
        qtyInput.setValue(String.valueOf(quantity));
        card.find("[data-action='add-to-cart']").click();
    }

    public void clickMakeOrder() {
        MainPageAssert mainPageAssert = new MainPageAssert(this);
        mainPageAssert.makeOrderButtonIsVisible();
        makeOrderButton.click();
    }

    public void isMakeOrderVisibility() {
        MainPageAssert mainPageAssert = new MainPageAssert(this);
        mainPageAssert.toastContainerIsVsibileAndText("Заказ принят в обработку");
    }

    public void clickCartButton() {
        MainPageAssert mainPageAssert = new MainPageAssert(this);
        mainPageAssert.cartButtonIsVisible();
        cartButton.click();
    }

    public void checkCartCountNumber(int expectedQuantity) {
        MainPageAssert mainPageAssert = new MainPageAssert(this);
        mainPageAssert.cartButtonIsVisible();
        cartButton.click();
        mainPageAssert.cartModalIsVisible();
        mainPageAssert.checkExpectedQuantity(expectedQuantity);
        mainPageAssert.closeModalIsVisible();
        closeModal.click();
    }

    public void checkCartPrice(double totalPrice) {
        MainPageAssert mainPageAssert = new MainPageAssert(this);
        mainPageAssert.cartButtonIsVisible();
        cartButton.click();
        mainPageAssert.cartModalIsVisible();
        mainPageAssert.checkTotalPrice(totalPrice);
        mainPageAssert.closeModalIsVisible();
        closeModal.click();
    }

    public SelenideElement getTotalPrice() {
        return totalPrice;
    }

    public SelenideElement getToastContainer() {
        return toastContainer;
    }

    public SelenideElement getCartModalWindow() {
        return cartModalWindow;
    }

    public SelenideElement getProductsList() {
        return productsList;
    }

    public ElementsCollection getProductCards() {
        return productCards;
    }

    public SelenideElement getMainTitle() {
        return mainTitle;
    }

    public SelenideElement getAdminLink() {
        return adminLink;
    }

    public SelenideElement getCartButton() {
        return cartButton;
    }

    public SelenideElement getCartCount() {
        return cartCount;
    }

    public SelenideElement getCloseModal() {
        return closeModal;
    }

    public SelenideElement getMakeOrderButton() {
        return makeOrderButton;
    }
}

