package ru.t1.authomatization.ui.homework9.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.assertj.core.api.AbstractAssert;

import static com.codeborne.selenide.Condition.visible;

public class LoginPageAssert extends AbstractAssert<LoginPageAssert,LoginPage> {
    protected LoginPageAssert(LoginPage loginPage, Class<?> selfType) {
        super(loginPage, selfType);
    }
    // Конструктор обязателен для AbstractAssert
    public LoginPageAssert(LoginPage actual) {
        super(actual, LoginPageAssert.class);
    }


    /**
     * Проверяет, что поле ввода имени пользователя видимо на странице
     */
    @Step("Проверяет, что поле ввода имени пользователя видимо на странице")
    public LoginPageAssert usernameIsVisible() {
        SelenideElement usernameField = actual.getUsernameInput();
        usernameField.shouldHave(visible);
        return this;
    }

    /**
     * Проверяет, что поле ввода пароля видимо на странице
     */
    @Step("Проверяет, что поле ввода пароля видимо на странице")
    public LoginPageAssert passwordIsVisible() {
        SelenideElement passwordField = actual.getPasswordInput();
        passwordField.shouldHave(visible);
        return this;
    }

    /**
     * Проверяет, что кнопка входа видима на странице
     */
    @Step("Проверяет, что кнопка входа видима на странице")
    public LoginPageAssert loginButtonIsVisible() {
        SelenideElement submitButton = actual.getSignInButton();
        submitButton.shouldHave(visible);
        return this;
    }

    /**
     * Пример комплексной проверки: все элементы формы логина видны
     */
    @Step("Пример комплексной проверки: все элементы формы логина видны")
    public LoginPageAssert formIsLoaded() {
        usernameIsVisible();
        passwordIsVisible();
        loginButtonIsVisible();
        return this;
    }
}
