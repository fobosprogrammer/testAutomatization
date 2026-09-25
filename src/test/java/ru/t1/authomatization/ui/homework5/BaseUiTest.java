package ru.t1.authomatization.ui.homework5;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import ru.t1.authomatization.ui.homework5.config.UiConfig;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static com.codeborne.selenide.Selenide.open;

@Tag("ui")
public abstract class BaseUiTest {

    /**
     * Настройка Selenide. Выполняется ОДИН раз перед всеми тестами класса.
     * Идемпотентно: сколько бы раз ни вызывалось — настройка применится
     * лишь один раз (см. UiConfig.init).
     */
    @BeforeAll
    static void configureSelenide() {
        UiConfig.init();
    }

    /**
     * «ХАППИ-ПАТ» ПЕРЕД КАЖДЫМ ТЕСТОМ:
     * открываем главную страницу магазина и убеждаемся, что URL корректный.
     *
     * Почему открываем именно "/"? Корзина в SmartShop живёт в переменной
     * JavaScript и сбрасывается при каждой загрузке страницы. Открыв "/",
     * мы гарантируем одинаковое стартовое состояние для каждого теста.
     */
    @BeforeEach
    void openShop() {
        open("/");
        // Сразу после открытия проверяем корректность URL (см. checkUrl).
        checkUrl("/");
    }

    /**
     * ОЧИСТКА ПОСЛЕ КАЖДОГО ТЕСТА (выполняется даже при падении теста).
     *
     * Что делаем:
     * 1) удаляем через API все товары, созданные в ходе теста
     *    (соблюдение принципа «удали за собой данные»);
     * 2) закрываем браузер — даёт ПОЛНУЮ изоляцию тестов:
     *    - свежая корзина (переменная JS пуста),
     *    - свежий сеанс авторизации (не «протекает» в следующий тест),
     *    - чистый DOM без «хвостов» предыдущего теста.
     */
    @AfterEach
    void cleanup() {
        // 1. Удаляем созданные товары. Не критично, если удаление вернёт
        //    ошибку (товара могло уже не быть) — главное, попытаться.
//        for (Long id : createdProductIds) {
//            try {
//                goodsApi.deleteProduct(id);
//            } catch (RuntimeException e) {
//                System.err.println("Не удалось удалить товар id=" + id + ": " + e.getMessage());
//            }
//        }
//        createdProductIds.clear();

        // 2. Закрываем браузер (заодно Selenide сохранит скриншот и HTML
        //    на случай, если тест упал — см. UiConfig).
        Selenide.closeWebDriver();
    }

    // =================================================================
    //  ПРОВЕРКИ URL (требование: «после каждого действия проверять URL»)
    // =================================================================

    /**
     * Проверяет, что текущий URL в браузере имеет ровно указанный путь.
     *
     * Примеры:
     * <pre>
     *      checkUrl("/")            — мы на главной странице
     *      checkUrl("/login")       — мы на странице входа
     *      checkUrl("/admin")       — мы в админке
     *  </pre>
     *
     * @param expectedPath ожидаемый путь, например {@code "/admin"}
     */
    protected void checkUrl(String expectedPath) {
        String currentUrl = WebDriverRunner.url();
        String actualPath = pathOf(currentUrl);
        assertThat(actualPath)
                .as("путь текущего URL: " + currentUrl)
                .isEqualTo(expectedPath);
    }

    /**
     * Проверяет, что путь текущего URL содержит подстроку.
     * Полезно, когда в URL есть query-параметры, например после неудачного
     * входа сервер редиректит на {@code /login?error}.
     *
     * @param part ожидаемая подстрока пути, например {@code "/login"}
     */
    protected void checkUrlContains(String part) {
        String currentUrl = WebDriverRunner.url();
        String actualPath = pathOf(currentUrl);
        assertThat(actualPath)
                .as("путь текущего URL: " + currentUrl)
                .contains(part);
    }

    private String pathOf(String url) {
        return URI.create(url).getPath();
    }
}
