package ru.t1.authomatization.ui.homework9.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {

    // Элементы формы авторизации
    private final SelenideElement usernameInput = $("#username");

    /** Поле «Пароль». Локатор: по id (#password). */
    private final SelenideElement passwordInput = $("#password");

    /** Кнопка «Sign in». Локатор: по атрибуту type (id у неё нет). */
    private final SelenideElement signInButton = $("button[type='submit']");

    /** Блок ошибки после неудачного входа. */
    private final SelenideElement errorAlert = $(".alert.alert-danger");

    /**
     * Открывает страницу входа напрямую.
     *
     * @return this
     */
    @Step("Открытие страницы логина")
    public LoginPage openPage() {
        open("/login");
        return this;
    }

    // =================================================================
    //  ВВОД ДАННЫХ
    // =================================================================

    /**
     * Вводит логин в поле #username.
     *
     * @param username логин
     * @return this
     */
    @Step("Ввести логин")
    public LoginPage enterUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    /**
     * Вводит пароль в поле #password.
     *
     * @param password пароль
     * @return this
     */
    @Step("Ввести пароль")
    public LoginPage enterPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    /**
     * Кликает «Sign in» — отправляет форму.
     *
     * @return this
     */
    @Step("Нажатие кнопки Sign in")
    public LoginPage clickSignIn() {
        signInButton.click();
        return this;
    }

    public SelenideElement getUsernameInput() {
        return usernameInput;
    }

    public SelenideElement getErrorAlert() {
        return errorAlert;
    }

    public SelenideElement getSignInButton() {
        return signInButton;
    }

    public SelenideElement getPasswordInput() {
        return passwordInput;
    }


    // =================================================================
    //  ГОТОВЫЕ СЦЕНАРИИ ВХОДА
    // =================================================================

    /**
     * Полный сценарий успешного входа: заполнить форму и нажать «Sign in».
     * После успешного входа сервер редиректит на /admin.
     *
     * ВАЖНО: после сабмита сервер выполняет цепочку редиректов
     * (/login -> 302 -> /admin). Чтобы проверка URL в тесте не «выстрелила»
     * раньше времени, явно ждём появления админки (Selenide опрашивает
     * страницу до таймаута Configuration.timeout).
     *
     * @param username логин
     * @param password пароль
     * @return Page Object админки {@link AdminPage}
     */
    @Step("Полный сценарий успешного входа: заполнить форму и нажать «Sign in»")
    public AdminPage loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickSignIn();
        // Ждём загрузки админки — теперь тест может смело проверять URL.
        AdminPage adminPage = new AdminPage();
        adminPage.checkPageDisplayed();
        return adminPage;
    }

    /**
     * Полный сценарий НЕуспешного входа: заполнить форму, нажать «Sign in»
     * и остаться на странице входа с блоком ошибки.
     *
     * @param username логин
     * @param password пароль
     * @return this (мы всё ещё на странице входа)
     */
    @Step("Полный сценарий НЕуспешного входа: заполнить форму, нажать «Sign in»")
    public LoginPage loginExpectingFailure(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickSignIn();
        return this;
    }

    // =================================================================
    //  ПРОВЕРКИ
    // =================================================================

    /**
     * Проверяет, что страница входа отобразилась (форма и поля видны).
     *
     * @return this
     */
    @Step("Проверяет, что страница входа отобразилась (форма и поля видны)")
    public LoginPage checkPageDisplayed() {
        LoginPageAssert loginPageAssert = new LoginPageAssert(this);
        loginPageAssert.formIsLoaded();
        return this;
    }

    /**
     * Проверяет, что показан блок ошибки авторизации.
     *
     * @param expectedMessage ожидаемый текст ошибки
     * @return this
     */
    @Step("Проверяет, что показан блок ошибки авторизации.")
    public LoginPage checkError(String expectedMessage) {
        errorAlert.shouldBe(visible)
                .shouldHave(com.codeborne.selenide.Condition.text(expectedMessage));
        return this;
    }
}
