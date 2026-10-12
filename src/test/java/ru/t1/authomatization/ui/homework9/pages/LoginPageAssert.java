package ru.t1.authomatization.ui.homework9.pages;

import com.codeborne.selenide.SelenideElement;
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
    public LoginPageAssert usernameIsVisible() {
        SelenideElement usernameField = actual.getUsernameInput();
        usernameField.shouldHave(visible);
        return this;
    }

    /**
     * Проверяет, что поле ввода пароля видимо на странице
     */
    public LoginPageAssert passwordIsVisible() {
        SelenideElement passwordField = actual.getPasswordInput();
        passwordField.shouldHave(visible);
        return this;
    }

    /**
     * Проверяет, что кнопка входа видима на странице
     */
    public LoginPageAssert loginButtonIsVisible() {
        SelenideElement submitButton = actual.getSignInButton();
        submitButton.shouldHave(visible);
        return this;
    }

    /**
     * Пример комплексной проверки: все элементы формы логина видны
     */
    public LoginPageAssert formIsLoaded() {
        usernameIsVisible();
        passwordIsVisible();
        loginButtonIsVisible();
        return this;
    }

//    // 2. Статическая точка входа (фабричный метод)
//    public static LoginPageAssert assertThat(LoginPage actual) {
//        return new LoginPageAssert(actual);
//    }
//
//    // 3. Кастомное утверждение: проверка, что кнопка или инпут задизейблены
//    public LoginPageAssert isDisabled() {
//        isNotNull(); // Проверка встроенным методом AssertJ, что элемент не null
//
//        if (actual.isEnabled()) {
//            failWithMessage("Ожидалось, что элемент задизейблен, но он доступен для взаимодействия!");
//        }
//        return this; // Возвращаем текущий объект для chaining-вызовов
//    }
//
//    // 4. Кастомное утверждение: проверка точного CSS класса
//    public LoginPageAssert hasClass(String cssClass) {
//        isNotNull();
//        String classAttribute = actual.getAttribute("class");
//
//        if (classAttribute == null || !classAttribute.contains(cssClass)) {
//            failWithMessage("Ожидался CSS-класс '%s', но элемент имеет классы: [%s]", cssClass, classAttribute);
//        }
//        return this;
//    }
//
//    // 5. Кастомное утверждение: проверка текста с игнорированием регистра
//    public LoginPageAssert hasTextIgnoringCase(String expectedText) {
//        isNotNull();
//        String actualText = actual.getText();
//
//        if (!actualText.equalsIgnoreCase(expectedText)) {
//            failWithMessage("Ожидался текст '%s' (без учета регистра), но элемент содержит: '%s'", expectedText, actualText);
//        }
//        return this;
//    }
}
